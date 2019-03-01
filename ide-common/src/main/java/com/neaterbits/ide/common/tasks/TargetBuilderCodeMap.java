package com.neaterbits.ide.common.tasks;

import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public final class TargetBuilderCodeMap extends TargetBuildSpec<CodeMapScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<CodeMapScanContext> targetBuilder) {

		targetBuilder.addTarget("codemap", "Build codemap");
		
	}
}
