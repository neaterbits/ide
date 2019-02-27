package com.neaterbits.ide.util.scheduling.dependencies.builder;

import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;

public interface TargetBuilder<CONTEXT extends TaskContext> {

	NoTargetPrerequisitesBuilder<CONTEXT> addTarget(String targetName, String description);
	
	TargetSpec<CONTEXT, ?, ?> build();
}
