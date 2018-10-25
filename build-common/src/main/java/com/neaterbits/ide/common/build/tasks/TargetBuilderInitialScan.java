package com.neaterbits.ide.common.build.tasks;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilderImpl;

public class TargetBuilderInitialScan {

	public static TargetSpec<InitialScanContext, ?, ?> makeTargetBuilder() {

		final TargetBuilder<InitialScanContext> targetBuilder = new TargetBuilderImpl<>();
		
		targetBuilder.target("sourcefolders", "Source folders for all modules")
				.prerequisites("Source folders")
				.iterating(InitialScanContext::getModules)
				.build(subTarget -> subTarget
						.target(ModuleResourcePath.class, "sourcefolders", module -> "Find source folders for " + module.getName())
						.actionWithResult(Constraint.IO, (context, module) -> context.buildRoot.getBuildSystemRootScan().findSourceFolders(module))
						.processResult((context, module, sourceFolders) -> context.buildRoot.setSourceFolders(module, sourceFolders))
				);
		
		return targetBuilder.build();
	}
}
