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
		
		targetBuilder.target("compileall", "Compile all modules")
			.prerequisites("Modules")
			.iterating(context -> context.buildRoot.getModules())
			.build(subTarget-> subTarget
					
				.target(ModuleResourcePath.class,
						CompiledModuleFileResourcePath.class,
						
						(context, module) -> context.buildRoot.getCompiledModuleFile(module),
						CompiledModuleFileResourcePath::getFile,
						module -> "Compile module  " + module.getName())

					.prerequisites("Module dependencies list")
						.product(ModuleDependencyList.class)
						.item(Dependency.class)
						.iterating(null, TargetBuilderModules::transitiveProjectDependencies)
						.collect((module, dependencyList) -> new ModuleDependencyList(module, dependencyList))

					.prerequisites("Project dependencies")
						.iterating(null, (context, module) -> transitiveProjectDependencies(context, module).stream()
								.filter(dependency -> dependency.getType() == DependencyType.PROJECT)
								.collect(Collectors.toList()))
						
						.build(st -> {
							st.target(
									Dependency.class,
									CompiledModuleFileResourcePath.class,
									(context, dependency) -> (CompiledModuleFileResourcePath)dependency.getResourcePath(),
									CompiledModuleFileResourcePath::getFile,
									dependency -> "Project dependency " + dependency.getResourcePath().getLast().getName());
						})

					.prerequisites("External dependencies")
						.iterating(null, (context, module) -> transitiveProjectDependencies(context, module).stream()
								.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
								.collect(Collectors.toList()))
						
						.build(st -> {
							st.target(
									Dependency.class,
									LibraryResourcePath.class,
									(context, dependency) -> (LibraryResourcePath)dependency.getResourcePath(),
									LibraryResourcePath::getFile,
									dependency -> "Project dependency " + dependency.getResourcePath().getLast().getName())

							.prerequisites("Transitive external dependencies")
							//.iterating(Constraint.IO, (context, dependency) -> context.buildRoot.getTransitiveExternalDependencies(dependency))

							//.buildReferSame()
							
							/*
							.action(Constraint.NETWORK, (context, target, actionParams) -> {
								
								context.buildRoot.downloadExternalDependency(target);
								
								//actionParams.
							})
							*/;
						})

					.prerequisites("Module class compile list")
						.product(ModuleCompileList.class)
						.item(SourceFolderCompileList.class)
						
						.iterating(Constraint.IO, (context, module) -> context.buildRoot.getBuildSystemRootScan().findSourceFolders(module))

						.build(st -> st
								
							.target(SourceFolderResourcePath.class, "compilelist", sourceFolder -> "Class files for source folder " + sourceFolder.getName())
							
								.prerequisites("Source folder compilations")
									.product(SourceFolderCompileList.class)
									.item(FileCompilation.class)
									.iterating(Constraint.IO, (ctx, sourceFolder) -> getSourceFiles(ctx, sourceFolder))
													
									.build(sourceFileTarget -> sourceFileTarget
										.target(FileCompilation.class, FileCompilation::getCompiledFile, classFile -> "Class file for source file " + classFile.getSourceFile().getName())
											.prerequisite("Source file")
											.from(FileCompilation::getSourcePath)
											.withFile(FileCompilation::getSourceFile)
									)
									.collect((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
						)
						.collect((module, resultList) -> new ModuleCompileList(module, resultList))
					
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
