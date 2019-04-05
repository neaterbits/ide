package com.neaterbits.ide.util.dependencyresolution;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public final class PrerequisiteSpec<CONTEXT extends TaskContext, TARGET, PREREQUISITE> {

	private final String named;
	
	private final String description;
	private final Class<?> productType;
	private final Class<?> itemType;
	private final Constraint constraint;
	private final BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites;
	private final BiFunction<CONTEXT, ?, Collection<PREREQUISITE>> getSubPrerequisites;
	private final Function<PREREQUISITE, TARGET> getDependencyFromPrerequisite;
	private final boolean recursiveBuild;
	private final BuildSpec<CONTEXT, PREREQUISITE> action;
	private final CollectSubTargets<TARGET> collectSubTargets;
	private final CollectSubProducts<TARGET> collectSubProducts;
	
	public PrerequisiteSpec(String named) {
		this(named, null, null, null, null, null, null, null, false, null, null, null);
	}

	public PrerequisiteSpec(
			String named,
			String description,
			Class<?> productType,
			Class<?> itemType,
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites,
			BiFunction<CONTEXT, ?, Collection<PREREQUISITE>> getSubPrerequisites,
			Function<PREREQUISITE, TARGET> getDependencyFromPrerequisite,
			boolean recursiveBuild,
			BuildSpec<CONTEXT, PREREQUISITE> action,
			CollectSubTargets<TARGET> collectSubTargets,
			CollectSubProducts<TARGET> collectSubProducts) {

		if (named == null) {
			Objects.requireNonNull(getPrerequisites);
		}
		else {
			if (named.isEmpty()) {
				throw new IllegalArgumentException();
			}
			
			if (!named.trim().equals(named)) {
				throw new IllegalArgumentException();
			}
		}
		
		if (recursiveBuild) {
			Objects.requireNonNull(getSubPrerequisites);
			Objects.requireNonNull(getDependencyFromPrerequisite);
			Objects.requireNonNull(action);
		}
		
		this.named = named;
		
		this.description = description;
		
		this.productType = productType;
		this.itemType = itemType;
		
		this.constraint = constraint;
		this.getPrerequisites = getPrerequisites;

		this.getSubPrerequisites = getSubPrerequisites;
		this.getDependencyFromPrerequisite = getDependencyFromPrerequisite;
		this.recursiveBuild = recursiveBuild;
		
		this.action = action;
		
		this.collectSubTargets = collectSubTargets;
		this.collectSubProducts = collectSubProducts;
	}
	
	String getNamed() {
		return named;
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
	
	BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisitesFunction() {
		return getPrerequisites;
	}
	
	BiFunction<CONTEXT, ?, Collection<PREREQUISITE>> getSubPrerequisitesFunction() {
		return getSubPrerequisites;
	}
	
	Function<PREREQUISITE, TARGET> getTargetFromPrerequisiteFunction() {
		
		return getDependencyFromPrerequisite;
	}

	boolean isRecursiveBuild() {
		return recursiveBuild;
	}

	BuildSpec<CONTEXT, PREREQUISITE> getAction() {
		return action;
	}

	CollectSubTargets<TARGET> getCollectSubTargets() {
		return collectSubTargets;
	}

	CollectSubProducts<TARGET> getCollectSubProducts() {
		return collectSubProducts;
	}
}
