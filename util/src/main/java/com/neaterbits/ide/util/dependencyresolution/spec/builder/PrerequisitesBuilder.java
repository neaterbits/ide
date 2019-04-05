package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisitesBuilder<CONTEXT extends TaskContext, TARGET> {

	TargetPrerequisitesBuilder<CONTEXT, TARGET> withPrerequisites(String description);

	TargetPrerequisiteBuilder<CONTEXT, TARGET> withPrerequisite(String description);

}
