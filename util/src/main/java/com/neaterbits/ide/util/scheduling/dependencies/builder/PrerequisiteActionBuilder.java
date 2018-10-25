package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.Consumer;

public interface PrerequisiteActionBuilder<CONTEXT extends TaskContext, TARGET, PREREQUISITE> {

	PrerequisitesOrActionBuilder<CONTEXT, TARGET> build(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);
	
}
