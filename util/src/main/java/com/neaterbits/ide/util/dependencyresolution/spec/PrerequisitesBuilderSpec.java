package com.neaterbits.ide.util.dependencyresolution.spec;

import com.neaterbits.ide.util.dependencyresolution.builder.PrerequisitesBuilder;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public abstract class PrerequisitesBuilderSpec<CONTEXT extends TaskContext, TARGET> {

	public abstract void buildSpec(PrerequisitesBuilder<CONTEXT, TARGET> builder);
	
}
