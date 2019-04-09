package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface TargetPrerequisiteBuilder<CONTEXT extends TaskContext, TARGET> {

	<PREREQUISITE>
	PrerequisiteFromBuilder<CONTEXT, PREREQUISITE> from(Function<TARGET, PREREQUISITE> from);
	
}
