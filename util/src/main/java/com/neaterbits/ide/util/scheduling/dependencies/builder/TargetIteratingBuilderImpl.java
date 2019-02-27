package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;

class TargetIteratingBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET>
	implements TargetIteratingBuilder<CONTEXT, TARGET> {

	private final String description;
	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	
	TargetIteratingBuilderImpl(TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState, String description) {
		
		Objects.requireNonNull(targetBuilderState);
		
		this.targetBuilderState = targetBuilderState;
		this.description = description;
	}
	
	final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> getTargetBuilderState() {
		return targetBuilderState;
	}

	@Override
	public final <PREREQUISITE> PrerequisiteActionBuilder<CONTEXT, TARGET, PREREQUISITE> fromIterating(
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites) {

		final PrerequisiteBuilderState<CONTEXT, TARGET, Void, Void> prerequisiteBuilderState = new PrerequisiteBuilderState<>(description, null, null);
		
		prerequisiteBuilderState.setIterating(constraint, getPrerequisites);
		
		targetBuilderState.addPrerequisiteBuilder(prerequisiteBuilderState);
		
		return new PrerequisiteActionBuilderImpl<>(targetBuilderState);
	}
}
