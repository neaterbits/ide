package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

final class Prerequisites {

	private final List<Prerequisite<?>> prerequisites;
	private final PrerequisiteSpec<?, ?, ?> spec;
	
	Prerequisites(List<Prerequisite<?>> prerequisites, PrerequisiteSpec<?, ?, ?> spec) {
		
		Objects.requireNonNull(prerequisites);
		Objects.requireNonNull(spec);
	
		this.prerequisites = prerequisites;
		this.spec = spec;
		
		prerequisites.forEach(prerequisite -> prerequisite.setFromPrerequisites(this));
	}

	List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}
	
	PrerequisiteSpec<?, ?, ?> getSpec() {
		return spec;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	BiFunction<?, ?, Collection<?>> getSubPrerequisites() {
		return (BiFunction)spec.getSubPrerequisites();
	}

	Function<?, ?> getTargetFromSubPrerequisite() {
		return spec.getTargetFromPrerequisite();
	}

	boolean isRecursiveBuild() {
		return spec.isRecursiveBuild();
	}

	BiFunction<?, List<?>, ?> getCollect() {
		return spec.getCollect();
	}

	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
