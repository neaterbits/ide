package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public final class PrerequisiteSpec<CONTEXT extends TaskContext, TARGET, PREREQUISITE> {

	private final String description;
	private final Class<?> productType;
	private final Class<?> itemType;
	private final Constraint constraint;
	private final BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites;
	private final BuildSpec<CONTEXT, PREREQUISITE> action;
	private final BiFunction<TARGET, List<?>, ?> collect;

	public PrerequisiteSpec(
			String description,
			Class<?> productType,
			Class<?> itemType,
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites,
			BuildSpec<CONTEXT, PREREQUISITE> action,
			BiFunction<TARGET, List<?>, ?> collect) {

		Objects.requireNonNull(getPrerequisites);
		
		this.description = description;
		
		this.productType = productType;
		this.itemType = itemType;
		
		this.constraint = constraint;
		this.getPrerequisites = getPrerequisites;

		this.action = action;
		
		this.collect = collect;
	}
	
	String getDescription() {
		return description;
	}

	Class<?> getProductType() {
		return productType;
	}

	Class<?> getItemType() {
		return itemType;
	}

	Constraint getConstraint() {
		return constraint;
	}

	Collection<PREREQUISITE> getPrerequisites(CONTEXT context, TARGET target) {
		
		Objects.requireNonNull(context);
		
		return getPrerequisites.apply(context, target);
	}

	BuildSpec<CONTEXT, PREREQUISITE> getAction() {
		return action;
	}

	BiFunction<TARGET, List<?>, ?> getCollect() {
		return collect;
	}
}
