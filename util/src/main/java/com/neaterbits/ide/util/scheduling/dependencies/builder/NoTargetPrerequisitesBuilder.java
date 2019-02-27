package com.neaterbits.ide.util.scheduling.dependencies.builder;


public interface NoTargetPrerequisitesBuilder<CONTEXT extends TaskContext> {

	NoTargetIteratingBuilder<CONTEXT> withPrerequisites(String description);

	NoTargetIteratingBuilder<CONTEXT> prerequisite(String description);
	
}
