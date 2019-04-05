package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisiteActionBuilder<CONTEXT extends TaskContext, TARGET, PREREQUISITE> {

	PrerequisitesOrActionBuilder<CONTEXT, TARGET> buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);
	
}
