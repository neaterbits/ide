package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface PrerequisitesBuilder<CONTEXT extends TaskContext, TARGET> {

	TargetPrerequisitesBuilder<CONTEXT, TARGET> withPrerequisites(String description);

	TargetPrerequisiteBuilder<CONTEXT, TARGET> withPrerequisite(String description);

}
