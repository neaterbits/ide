package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.Consumer;

public interface NoTargetPrerequisiteBuilder<CONTEXT extends TaskContext, PREREQUISITE> {

	void buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);

}
