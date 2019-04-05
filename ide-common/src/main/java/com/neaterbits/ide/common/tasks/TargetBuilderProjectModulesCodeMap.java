package com.neaterbits.ide.common.tasks;

import com.neaterbits.ide.common.build.model.compile.FileCompilation;
import com.neaterbits.ide.common.build.tasks.SourceFilesBuilderUtil;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public final class TargetBuilderProjectModulesCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget("projectcodemap", "Add project module build files to codemap")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.getModules())
			.buildBy(subTarget-> subTarget
					.addInfoSubTarget(
							ProjectModuleResourcePath.class,
							"module",
							module -> module.getName(),
							module ->"Codemap for module " + module.getName())
					
					.withPrerequisites("Module class files")
					.fromIterating(Constraint.IO, (context, module) -> context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module))
					.buildBy(st -> st
							
						.addInfoSubTarget(
								SourceFolderResourcePath.class,
								"compilelist",
								sourceFolder -> sourceFolder.getModule().getName(),
								sourceFolder -> "Class files for source folder " + sourceFolder.getName())

							.withPrerequisites("Source folder compilations")
							.fromIterating(Constraint.IO, (ctx, sourceFolder) -> SourceFilesBuilderUtil.getSourceFiles(ctx, sourceFolder))
							.buildBy(sourceFileTarget -> sourceFileTarget
										
										.addInfoSubTarget(
												FileCompilation.class,
												"classcodemap",
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
