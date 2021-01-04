package com.neaterbits.ide.common.tasks;

import com.neaterbits.build.strategies.common.SourceFilesBuilderUtil;
import com.neaterbits.build.types.compile.FileCompilation;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class TargetBuilderProjectModulesCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(
		        "projectcodemap",
		        "project_codemap",
		        "add_buildfiles_to_codemap",
		        "Add project module build files to codemap")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.getModules())
			.buildBy(subTarget-> subTarget
					.addInfoSubTarget(
							ProjectModuleResourcePath.class,
			                "module_codemap",
			                "add_modules_buildfiles_to_codemap",
							module -> module.getName(),
							module ->"Codemap for module " + module.getName())
					
					.withPrerequisites("Module class files")
					.fromIterating(
					        Constraint.IO,
					        (context, module) -> context.getBuildRoot()
					                                    .getBuildSystemRootScan()
					                                    .findSourceFolders(module))
					.buildBy(st -> st
							
						.addInfoSubTarget(
								SourceFolderResourcePath.class,
                                "module_compilelist",
                                "get_module_compilelist",
								sourceFolder -> sourceFolder.getModule().getName(),
								sourceFolder -> "Class files for source folder " + sourceFolder.getName())

							.withPrerequisites("Source folder compilations")
							.fromIterating(Constraint.IO, (ctx, sourceFolder) -> SourceFilesBuilderUtil.getSourceFiles(ctx, sourceFolder))
							.buildBy(sourceFileTarget -> sourceFileTarget
										
										.addInfoSubTarget(
												FileCompilation.class,
												"class_codemap",
												"generate_class_codemap",
												fileCompilation -> fileCompilation.getSourceFile().getPath(),
												fileCompilation -> "Generate codemap for " + fileCompilation.getCompiledFile().getPath())
										
										.action(Constraint.IO, (ctx, target, actionParameters) -> {
											ctx.getCodeMapGatherer().addClassFile(target);
											
											return null;
										})
								)
					)
			);
	}
}
