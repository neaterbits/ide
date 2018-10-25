package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.BuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisiteSpec;

final class PrerequisiteBuilderState<CONTEXT extends TaskContext, TARGET, PRODUCT, ITEM> {

	private final String description;
	private final Class<PRODUCT> productType;
	private final Class<ITEM> itemType;

	private Constraint constraint;
	private BiFunction<CONTEXT, TARGET, Collection<?>> getPrerequisites;

	private BiFunction<TARGET, List<ITEM>, PRODUCT> collect;

	private BuildSpec<CONTEXT, ?> build;
	
	PrerequisiteBuilderState(String description, Class<PRODUCT> productType, Class<ITEM> itemType) {
		
		Objects.requireNonNull(description);
		
		this.description = description;
		this.productType = productType;
		this.itemType = itemType;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	final <PREREQUISITE> void setIterating(
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites) {
		
		Objects.requireNonNull(getPrerequisites);

		this.constraint = constraint;
		this.getPrerequisites = (BiFunction)getPrerequisites;
	}

	final void setCollect(BiFunction<TARGET, List<ITEM>, PRODUCT> collect) {

		Objects.requireNonNull(collect);
		
		if (this.collect != null) {
			throw new IllegalStateException();
		}
		
		this.collect = collect;
	}

	final void setBuild(BuildSpec<CONTEXT, ?> build) {
		
		Objects.requireNonNull(build);
		
		this.build = build;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	final PrerequisiteSpec<CONTEXT, TARGET, ?> build() {
		return new PrerequisiteSpec<>(
				description,
				productType,
				itemType,
				constraint,
				(BiFunction)getPrerequisites,
				build,
				(BiFunction)collect);
	}
}
