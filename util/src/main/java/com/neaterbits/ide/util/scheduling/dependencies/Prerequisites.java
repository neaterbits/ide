package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

final class Prerequisites extends BuildEntity {

	private final List<Prerequisite<?>> prerequisites;
	private final PrerequisiteSpec<?, ?, ?> spec;

	private Target<?> fromTarget;

	Prerequisites(List<Prerequisite<?>> prerequisites, PrerequisiteSpec<?, ?, ?> spec) {
		
		Objects.requireNonNull(prerequisites);
		Objects.requireNonNull(spec);
	
		this.prerequisites = prerequisites;
		this.spec = spec;
		
		prerequisites.forEach(prerequisite -> prerequisite.setFromPrerequisites(this));
	}
	
	@Override
	String getDebugString() {
		return null;
	}

	@Override
	BuildEntity getFromEntity() {
		return fromTarget;
	}

	Target<?> getFromTarget() {
		return fromTarget;
	}

	void setFromTarget(Target<?> fromTarget) {
		
		Objects.requireNonNull(fromTarget);
		
		this.fromTarget = fromTarget;
	}

	List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}
	
	PrerequisiteSpec<?, ?, ?> getSpec() {
		return spec;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	BiFunction<?, ?, Collection<?>> getPrerequisitesFunction() {
		return (BiFunction)spec.getPrerequisitesFunction();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	BiFunction<?, ?, Collection<?>> getSubPrerequisitesFunction() {
		return (BiFunction)spec.getSubPrerequisitesFunction();
	}

	Function<?, ?> getTargetFromSubPrerequisite() {
		return spec.getTargetFromPrerequisiteFunction();
	}

	boolean isRecursiveBuild() {
		return spec.isRecursiveBuild();
	}

	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
