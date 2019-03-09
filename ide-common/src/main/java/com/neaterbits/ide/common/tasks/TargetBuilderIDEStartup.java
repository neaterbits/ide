package com.neaterbits.ide.common.tasks;

import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public final class TargetBuilderIDEStartup extends TargetBuildSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(new TargetBuilderGetSourceFolders());
		
		targetBuilder.addTarget(new TargetBuilderProjectModulesCodeMap())
				
				// Run after getting source folders so that IDE opens faster
				// .withNamedPrerequisite(TargetBuilderGetSourceFolders.NAME)
				;
		
		targetBuilder.addTarget(new TargetBuilderAddLibraryTypesToCodeMap());
		
		targetBuilder.addTarget(new TargetBuilderAddSystemLibraryTypeNamesToCodeMap());
		targetBuilder.addTarget(new TargetBuilderAddSystemLibraryTypesToCodeMap());
	}
}
