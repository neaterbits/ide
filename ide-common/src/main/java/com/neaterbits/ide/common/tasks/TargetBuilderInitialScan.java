package com.neaterbits.ide.common.tasks;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public class TargetBuilderInitialScan extends TargetBuildSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget("sourcefolders", "Source folders for all modules")
			.withPrerequisites("Source folders")
			.fromIterating(InitialScanContext::getModules)
			.buildBy(subTarget -> subTarget
					.addInfoSubTarget(ModuleResourcePath.class, "sourcefolders", module -> "Find source folders for " + module.getName())
					.actionWithResult(Constraint.IO, (context, module) -> context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module))
					.processResult((context, module, sourceFolders) -> context.getBuildRoot().setSourceFolders(module, sourceFolders))
			);
			
	}
}
