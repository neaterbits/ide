package com.neaterbits.ide.util.dependencyresolution.builder;

import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface NoTargetPrerequisiteBuilder<CONTEXT extends TaskContext, PREREQUISITE> {

	void buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);

	
}
