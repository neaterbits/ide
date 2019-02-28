package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface TargetBuilder<CONTEXT extends TaskContext> {

	NoTargetPrerequisitesBuilder<CONTEXT> addTarget(String targetName, String description);
	
}
