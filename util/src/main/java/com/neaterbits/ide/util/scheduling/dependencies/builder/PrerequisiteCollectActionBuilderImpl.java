package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.dependencies.BuildSpec;

class PrerequisiteCollectActionBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE, PRODUCT, ITEM>
		implements PrerequisiteCollectActionBuilder<CONTEXT, TARGET, PREREQUISITE, PRODUCT, ITEM> {

	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	private final PrerequisiteBuilderState<CONTEXT, TARGET, PRODUCT, ITEM> prerequisiteBuilderState;

	PrerequisiteCollectActionBuilderImpl(
			TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState,
			PrerequisiteBuilderState<CONTEXT, TARGET, PRODUCT, ITEM> prerequisiteBuilderState) {

		Objects.requireNonNull(targetBuilderState);
		Objects.requireNonNull(prerequisiteBuilderState);
		
		this.targetBuilderState = targetBuilderState;
		this.prerequisiteBuilderState = prerequisiteBuilderState;
	}

	@Override
	public PrerequisitesOrCollectBuilder<CONTEXT, TARGET, PRODUCT, ITEM> build(
			Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets) {

		final TypedSubTargetBuilderImpl<CONTEXT, PREREQUISITE> typedSubTargetBuilder = new TypedSubTargetBuilderImpl<>();
		
		prerequisiteTargets.accept(typedSubTargetBuilder);
		
		final TargetBuilderState<CONTEXT, PREREQUISITE, ?> subTargetState = typedSubTargetBuilder.build();
		
		prerequisiteBuilderState.setBuild(new BuildSpec<>(subTargetState.build()));
		
		return new PrerequisitesOrCollectBuilderImpl<>(targetBuilderState, prerequisiteBuilderState);
	}

	@Override
	public PrerequisitesBuilder<CONTEXT, TARGET> collect(BiFunction<TARGET, List<ITEM>, PRODUCT> collect) {

		prerequisiteBuilderState.setCollect(collect);
		
		return new PrerequisitesBuilderImpl<>(targetBuilderState);
	}
}
