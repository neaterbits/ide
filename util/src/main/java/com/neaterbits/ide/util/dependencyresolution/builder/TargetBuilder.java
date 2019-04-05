package com.neaterbits.ide.util.dependencyresolution.builder;

import com.neaterbits.ide.util.dependencyresolution.TargetBuilderSpec;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface TargetBuilder<CONTEXT extends TaskContext> {

	NoTargetPrerequisitesBuilder<CONTEXT> addTarget(String targetName, String description);
	
	NoTargetTargetBuildSpecPrerequisitesBuilder addTarget(TargetBuilderSpec<CONTEXT> subTarget);
}
