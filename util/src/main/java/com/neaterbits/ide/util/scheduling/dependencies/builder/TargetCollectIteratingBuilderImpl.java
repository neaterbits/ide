package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;

class TargetCollectIteratingBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET, PRODUCT, ITEM>
	implements TargetCollectIteratingBuilder<CONTEXT, TARGET, PRODUCT, ITEM> {

	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	private final PrerequisiteBuilderState<CONTEXT, TARGET, PRODUCT, ITEM> prerequisiteBuilderState;
	
	TargetCollectIteratingBuilderImpl(TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState, String description, Class<PRODUCT> productType, Class<ITEM> itemType) {
		
		this.targetBuilderState = targetBuilderState;
		this.prerequisiteBuilderState = new PrerequisiteBuilderState<>(description, productType, itemType);
		
		targetBuilderState.addPrerequisiteBuilder(prerequisiteBuilderState);
	}
	
	@Override
	public <PREREQUISITE> PrerequisiteCollectActionBuilder<CONTEXT, TARGET, PREREQUISITE, PRODUCT, ITEM> fromIterating(
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites) {
		
		prerequisiteBuilderState.setIterating(constraint, getPrerequisites);
		
		return new PrerequisiteCollectActionBuilderImpl<>(targetBuilderState, prerequisiteBuilderState);
	}
}
