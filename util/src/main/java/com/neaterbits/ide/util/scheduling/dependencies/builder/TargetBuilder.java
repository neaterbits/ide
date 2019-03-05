package com.neaterbits.ide.util.scheduling.dependencies.builder;

import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;

public interface TargetBuilder<CONTEXT extends TaskContext> {

	NoTargetPrerequisitesBuilder<CONTEXT> addTarget(String targetName, String description);
	
	NoTargetTargetBuildSpecPrerequisitesBuilder addTarget(TargetBuildSpec<CONTEXT> subTarget);
}
