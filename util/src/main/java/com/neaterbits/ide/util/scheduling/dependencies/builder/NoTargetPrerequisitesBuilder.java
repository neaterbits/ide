package com.neaterbits.ide.util.scheduling.dependencies.builder;


public interface NoTargetPrerequisitesBuilder<CONTEXT extends TaskContext> {

	NoTargetIteratingBuilder<CONTEXT> prerequisites(String description);

	NoTargetIteratingBuilder<CONTEXT> prerequisite(String description);
	
}
