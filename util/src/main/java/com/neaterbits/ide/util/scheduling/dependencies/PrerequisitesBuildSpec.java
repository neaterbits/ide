package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.PrerequisitesBuilder;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public abstract class PrerequisitesBuildSpec<CONTEXT extends TaskContext, TARGET> {

	public abstract void buildSpec(PrerequisitesBuilder<CONTEXT, TARGET> builder);
	
}
