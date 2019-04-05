package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public final class CollectSubTargets<TARGET> {

	private final Class<?> productClass;
	private final BiFunction<TARGET, List<?>, ?> collect;

	public CollectSubTargets(Class<?> productClass, BiFunction<TARGET, List<?>, ?> collect) {
		
		Objects.requireNonNull(productClass);
		Objects.requireNonNull(collect);
		
		this.productClass = productClass;
		this.collect = collect;
	}
	
	CollectedProduct collect(Object targetObject, CollectedTargetObjects subTargetObjects) {

		Objects.requireNonNull(targetObject);
		Objects.requireNonNull(subTargetObjects);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final BiFunction<Object, List<Object>, Object> function = (BiFunction)collect;
		
		final Object productObject =  function.apply(targetObject, subTargetObjects.getCollectedObjects());
		
		
		if (productObject == null) {
			throw new IllegalStateException();
		}
		
		if (!productObject.getClass().equals(productClass)) {
			throw new IllegalStateException();
		}
		
		return new CollectedProduct(productClass, productObject);
	}
}
