package com.neaterbits.ide.common.build.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.compile.BuildException;
import com.neaterbits.ide.common.build.compile.Compiler;
import com.neaterbits.ide.common.build.compile.CompilerStatus;
import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.DependencyType;
import com.neaterbits.ide.common.build.model.compile.FileCompilation;
import com.neaterbits.ide.common.build.model.compile.ModuleCompileList;
import com.neaterbits.ide.common.build.model.compile.ModuleDependencyList;
import com.neaterbits.ide.common.build.model.compile.SourceFolderCompileList;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilderImpl;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public class TargetBuilderModules {

	public static TargetSpec<ModulesBuildContext, ?, ?> makeTargetBuilderModules() {
		
		final TargetBuilderImpl<ModulesBuildContext> targetBuilder = new TargetBuilderImpl<>();
		
		targetBuilder.addTarget("compileall", "Compile all modules")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.buildRoot.getModules())
			.buildBy(subTarget-> subTarget
					
				.addFileSubTarget(ModuleResourcePath.class,
						CompiledModuleFileResourcePath.class,
						
						(context, module) -> context.buildRoot.getCompiledModuleFile(module),
						CompiledModuleFileResourcePath::getFile,
						module -> "Compile module  " + module.getName())

					// collect dependencies in list for later
					.withPrerequisites("Module dependencies list")
						.makingProduct(ModuleDependencyList.class)
						.fromItemType(Dependency.class)
						.fromIterating(null, TargetBuilderModules::transitiveProjectDependencies)
						.collectToProduct((module, dependencyList) -> new ModuleDependencyList(module, dependencyList))

					// add targets for local module dependencies
					.withPrerequisites("Project dependencies")
						.fromIterating(null, (context, module) -> transitiveProjectDependencies(context, module).stream()
								.filter(dependency -> dependency.getType() == DependencyType.PROJECT)
								.collect(Collectors.toList()))
						
						.buildBy(st -> {
							st.addFileSubTarget(
									Dependency.class,
									CompiledModuleFileResourcePath.class,
									(context, dependency) -> (CompiledModuleFileResourcePath)dependency.getResourcePath(),
									CompiledModuleFileResourcePath::getFile,
									dependency -> "Project dependency " + dependency.getResourcePath().getLast().getName());
						})

					// for downloading external dependencies
					.withPrerequisites("External dependencies")
						.fromIterating(null, (context, module) -> transitiveProjectDependencies(context, module).stream()
								.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
								.collect(Collectors.toList()))
						
						.buildBy(st -> {
							st.addFileSubTarget(
									Dependency.class,
									LibraryResourcePath.class,
									(context, dependency) -> (LibraryResourcePath)dependency.getResourcePath(),
									LibraryResourcePath::getFile,
									dependency -> "External dependency " + dependency.getResourcePath().getLast().getName())

							.withPrerequisites("Transitive external dependencies")
							
							//.iterating(Constraint.IO, (context, dependency) -> context.buildRoot.getTransitiveExternalDependencies(dependency))

							//.buildReferSame()
							
							/*
							.action(Constraint.NETWORK, (context, target, actionParams) -> {
								
								context.buildRoot.downloadExternalDependency(target);
								
								//actionParams.
							})
							*/;
						})

					// must collect info on classes to compile into a list
					// so can run compiler onto multiple files
					.withPrerequisites("Module class compile list")
						.makingProduct(ModuleCompileList.class)
						.fromItemType(SourceFolderCompileList.class)
						
						.fromIterating(Constraint.IO, (context, module) -> context.buildRoot.getBuildSystemRootScan().findSourceFolders(module))

						.buildBy(st -> st
								
							.addInfoSubTarget(SourceFolderResourcePath.class, "compilelist", sourceFolder -> "Class files for source folder " + sourceFolder.getName())
							
								.withPrerequisites("Source folder compilations")
									.makingProduct(SourceFolderCompileList.class)
									.fromItemType(FileCompilation.class)
									.fromIterating(Constraint.IO, (ctx, sourceFolder) -> getSourceFiles(ctx, sourceFolder))
													
									.buildBy(sourceFileTarget -> sourceFileTarget
										.addFileSubTarget(FileCompilation.class, FileCompilation::getCompiledFile, classFile -> "Class file for source file " + classFile.getSourceFile().getName())
											.withPrerequisite("Source file")
											.from(FileCompilation::getSourcePath)
											.withFile(FileCompilation::getSourceFile)
									)
									.collectToProduct((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
						)
						.collectToProduct((module, resultList) -> new ModuleCompileList(module, resultList))
					
					.action(Constraint.CPU, (context, target, actionParameters) -> {
						
						final ModuleCompileList moduleCompileList = actionParameters.getCollected(ModuleCompileList.class);
						
						if (!moduleCompileList.getSourceFiles().isEmpty()) {
						
							final File targetDirectory = context.buildRoot.getTargetDirectory(target).getFile();
							final ModuleDependencyList moduleDependencyList = actionParameters.getCollected(ModuleDependencyList.class);
	
							compileSourceFiles(context.compiler, moduleCompileList, targetDirectory, moduleDependencyList);
						}
					})
			);
		
		return targetBuilder.build();
	}

	private static List<Dependency> transitiveProjectDependencies(ModulesBuildContext context, ModuleResourcePath module) {
		
		final List<Dependency> dependencies = new ArrayList<>();
		
		transitiveProjectDependencies(context, module, dependencies);

		return dependencies;
	}

	private static void transitiveProjectDependencies(ModulesBuildContext context, ModuleResourcePath module, List<Dependency> dependencies) {
		 
		final List<Dependency> moduleDependencies = context.buildRoot.getDependenciesForModule(module);
		 
		dependencies.addAll(moduleDependencies);

		for (Dependency dependency : moduleDependencies) {
			if (dependency.getType() == DependencyType.PROJECT) {
				transitiveProjectDependencies(context, dependency.getModule(), dependencies);
			}
		}
	}

	private static void compileSourceFiles(Compiler compiler, ModuleCompileList moduleCompileList, File targetDirectory, ModuleDependencyList moduleDependencyList) throws IOException, BuildException {

		if (moduleCompileList == null) {
			throw new IllegalStateException();
		}

		final CompilerStatus status = compiler.compile(

				moduleCompileList.getSourceFiles().stream()
					.flatMap(sourceFiles -> sourceFiles.getFileCompilations().stream())
					.map(fileCompilation -> fileCompilation.getSourcePath())
					.collect(Collectors.toList()),
					
				targetDirectory,
				
				moduleDependencyList.getDependencies().stream()
					.map(Dependency::getCompiledModuleFile)
					.collect(Collectors.toList()));

		if (!status.executedOk()) {
			throw new BuildException(status.getIssues());
		}
	}

	
	private static final List<FileCompilation> getSourceFiles(ModulesBuildContext context, SourceFolderResourcePath sourceFolder) {
		
		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();

		SourceFileScanner.findSourceFiles(sourceFolder, sourceFiles);

		final TargetDirectoryResourcePath targetDirectory = context.buildRoot.getTargetDirectory(sourceFolder.getModule());

		return sourceFiles.stream()
				.map(sourceFile -> new FileCompilation(sourceFile, context.language.getCompiledFilePath(targetDirectory, sourceFile)))
				.collect(Collectors.toList());
	}
}
