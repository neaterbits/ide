package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Objects;
import java.util.function.Consumer;

class PrerequisiteActionBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE>
		implements PrerequisiteActionBuilder<CONTEXT, TARGET, PREREQUISITE> {

	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	
	PrerequisiteActionBuilderImpl(TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState) {

		Objects.requireNonNull(targetBuilderState);
		
		this.targetBuilderState = targetBuilderState;
	}

	@Override
	public PrerequisitesOrActionBuilder<CONTEXT, TARGET> buildBy(
			Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets) {

		final TypedSubTargetBuilderImpl<CONTEXT, PREREQUISITE> typedSubTargetBuilder = new TypedSubTargetBuilderImpl<>();
		
		prerequisiteTargets.accept(typedSubTargetBuilder);
		
		return new PrerequisitesOrActionBuilderImpl<CONTEXT, TARGET, FILE_TARGET>(targetBuilderState);
	}
}
