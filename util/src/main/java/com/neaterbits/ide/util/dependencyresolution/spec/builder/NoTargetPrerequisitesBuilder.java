package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface NoTargetPrerequisitesBuilder<CONTEXT extends TaskContext> {

	NoTargetIteratingBuilder<CONTEXT> withPrerequisites(String description);

	NoTargetIteratingBuilder<CONTEXT> prerequisite(String description);
	
}
