package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Objects;
import java.util.function.Function;

final class TargetPrerequisiteBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET>
		implements TargetPrerequisiteBuilder<CONTEXT, TARGET> {

	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	private final String description;

	TargetPrerequisiteBuilderImpl(TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState, String description) {
		
		Objects.requireNonNull(targetBuilderState);
		Objects.requireNonNull(description);
		
		this.targetBuilderState = targetBuilderState;
		this.description = description;
	}

	@Override
	public <PREREQUISITE> PrerequisiteFromBuilder<CONTEXT, TARGET> from(Function<TARGET, PREREQUISITE> from) {
		return new PrerequisiteFromBuilderImpl<>(description);
	}
}
