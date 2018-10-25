package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface PrerequisitesBuilder<CONTEXT extends TaskContext, TARGET> {

	TargetPrerequisitesBuilder<CONTEXT, TARGET> prerequisites(String description);

	TargetPrerequisiteBuilder<CONTEXT, TARGET> prerequisite(String description);

}
