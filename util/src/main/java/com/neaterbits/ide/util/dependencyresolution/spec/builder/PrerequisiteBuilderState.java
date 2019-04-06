package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectSubProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectSubTargets;
import com.neaterbits.ide.util.dependencyresolution.executor.Collectors;
import com.neaterbits.ide.util.dependencyresolution.executor.RecursiveBuildInfo;
import com.neaterbits.ide.util.dependencyresolution.spec.BuildSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisiteSpec;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class PrerequisiteBuilderState<CONTEXT extends TaskContext, TARGET, PRODUCT, ITEM> {

	private final String description;
	private final Class<PRODUCT> productType;
	private final Class<ITEM> itemType;

	private Constraint constraint;
	private BiFunction<CONTEXT, TARGET, Collection<?>> getPrerequisites;
	private BiFunction<CONTEXT, ?, Collection<?>> getSubPrerequisites;
	private Function<?, ?> getDependencyFromPrerequisite;
	
	private boolean recursiveBuild;
	
	private BiFunction<TARGET, List<ITEM>, PRODUCT> collectSubTargets;
	private BiFunction<TARGET, List<ITEM>, PRODUCT> collectSubProducts;

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

		if (this.getPrerequisites != null) {
			throw new IllegalStateException();
		}
		
		this.constraint = constraint;
		this.getPrerequisites = (BiFunction)getPrerequisites;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final <PREREQUISITE, SUB_TARGET> void setIteratingAndBuildingRecursively(
			Constraint constraint,
			BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites,
			BiFunction<CONTEXT, SUB_TARGET, Collection<PREREQUISITE>> getSubPrerequisites,
			Function<PREREQUISITE, SUB_TARGET> getDependencyFromPrerequisite) {
		
		Objects.requireNonNull(getPrerequisites);
		Objects.requireNonNull(getDependencyFromPrerequisite);

		if (this.getPrerequisites != null) {
			throw new IllegalStateException();
		}

		this.constraint = constraint;
		this.getPrerequisites = (BiFunction)getPrerequisites;
		this.getSubPrerequisites = (BiFunction)getSubPrerequisites;
		this.getDependencyFromPrerequisite = getDependencyFromPrerequisite;
		this.recursiveBuild = true;
	}
	
	final void setCollectSubTargets(BiFunction<TARGET, List<ITEM>, PRODUCT> collect) {

		Objects.requireNonNull(collect);
		
		if (this.collectSubTargets != null) {
			throw new IllegalStateException();
		}
		
		this.collectSubTargets = collect;
	}

	final void setCollectSubProducts(BiFunction<TARGET, List<ITEM>, PRODUCT> collect) {

		Objects.requireNonNull(collect);
		
		if (this.collectSubProducts != null) {
			throw new IllegalStateException();
		}
		
		this.collectSubProducts = collect;
	}

	final void setBuild(BuildSpec<CONTEXT, ?> build) {
		
		Objects.requireNonNull(build);
		
		this.build = build;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	final PrerequisiteSpec<CONTEXT, TARGET, ?> build() {
		return new PrerequisiteSpec<>(
				null,
				description,
				productType,
				itemType,
				constraint,
				(BiFunction)getPrerequisites,
				recursiveBuild
					? new RecursiveBuildInfo<>(
							(BiFunction)getSubPrerequisites,
							(Function)getDependencyFromPrerequisite)
					: null
,
				build,
				new Collectors<>(
					collectSubTargets != null ? new CollectSubTargets<>(productType, (BiFunction)collectSubTargets) : null,
					collectSubProducts != null ? new CollectSubProducts<>(productType, (BiFunction)collectSubProducts) : null));
	}
}