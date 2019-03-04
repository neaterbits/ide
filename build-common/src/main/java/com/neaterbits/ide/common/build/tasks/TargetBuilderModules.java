package com.neaterbits.ide.common.build.tasks;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.compile.BuildException;
import com.neaterbits.ide.common.build.compile.Compiler;
import com.neaterbits.ide.common.build.compile.CompilerStatus;
import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.compile.ModuleCompileList;
import com.neaterbits.ide.common.build.model.compile.ModuleDependencyList;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public class TargetBuilderModules extends TargetBuildSpec<ModulesBuildContext> {

	public static final String TARGET_COMPILEALL = "compileall";
	
	@Override
	protected void buildSpec(TargetBuilder<ModulesBuildContext> targetBuilder) {
		
		targetBuilder.addTarget(TARGET_COMPILEALL, "Compile all modules")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.getModules())
			.buildBy(subTarget-> subTarget
					
				.addFileSubTarget(ProjectModuleResourcePath.class,
						CompiledModuleFileResourcePath.class,
						
						(context, module) -> context.getCompiledModuleFile(module),
						CompiledModuleFileResourcePath::getFile,
						module -> "Compile module  " + module.getName())

					// collect dependencies in list for later
					.withPrerequisites("Module dependencies list")
						.makingProduct(ModuleDependencyList.class)
						.fromItemType(Dependency.class)
						.fromIterating(null, ModuleBuilderUtil::transitiveProjectDependencies)
						.collectToProduct((module, dependencyList) -> new ModuleDependencyList(module, dependencyList))

					// add targets for local module dependencies
					.withPrerequisites(new PrerequisitesBuilderProjectDependencies())

					// for downloading external dependencies
					.withPrerequisites(new PrerequisitesBuilderExternalDependencies())
					
					// must collect info on classes to compile into a list
					// so can run compiler onto multiple files
					.withPrerequisites(new PrerequisitesBuilderModuleCompileList())
					
					.action(Constraint.CPU, (context, target, actionParameters) -> {
						
						final ModuleCompileList moduleCompileList = actionParameters.getCollected(ModuleCompileList.class);
						
						if (!moduleCompileList.getSourceFiles().isEmpty()) {
						
							final File targetDirectory = context.getBuildRoot().getTargetDirectory(target).getFile();
							final ModuleDependencyList moduleDependencyList = actionParameters.getCollected(ModuleDependencyList.class);
	
							compileSourceFiles(context.compiler, moduleCompileList, targetDirectory, moduleDependencyList);
						}
					})
			);
		
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
}
