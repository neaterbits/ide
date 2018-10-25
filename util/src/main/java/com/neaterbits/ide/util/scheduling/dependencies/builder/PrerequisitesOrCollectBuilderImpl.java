package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.List;
import java.util.function.BiFunction;

final class PrerequisitesOrCollectBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET, PRODUCT, ITEM>
		extends PrerequisitesBuilderImpl<CONTEXT, TARGET, FILE_TARGET>
		implements PrerequisitesOrCollectBuilder<CONTEXT, TARGET, PRODUCT, ITEM> {

	private final PrerequisiteBuilderState<CONTEXT, TARGET, PRODUCT, ITEM> prerequisiteBuilderState;


	PrerequisitesOrCollectBuilderImpl(
			TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState,
			PrerequisiteBuilderState<CONTEXT, TARGET, PRODUCT, ITEM> prerequisiteBuilderState) {
		
		super(targetBuilderState);

		this.prerequisiteBuilderState = prerequisiteBuilderState;
	}

	@Override
	public PrerequisitesOrActionBuilder<CONTEXT, TARGET>
		collect(BiFunction<TARGET, List<ITEM>, PRODUCT> collect) {

		prerequisiteBuilderState.setCollect(collect);
		
		return new PrerequisitesOrActionBuilderImpl<>(getTargetBuilderState());
	}
}
