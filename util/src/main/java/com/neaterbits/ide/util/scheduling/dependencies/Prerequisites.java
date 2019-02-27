package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

final class Prerequisites {

	private final List<Prerequisite<?>> prerequisites;
	private final Function<?, ?> targetFromPrerequisite;
	private final boolean recursiveBuild;
	private final BiFunction<?, List<?>, ?> collect;

	Prerequisites(List<Prerequisite<?>> prerequisites, Function<?, ?> targetFromPrerequisite, boolean recursiveBuild, BiFunction<?, List<?>, ?> collect) {
		
		Objects.requireNonNull(prerequisites);
		
		this.prerequisites = prerequisites;
		this.targetFromPrerequisite = targetFromPrerequisite;
		this.recursiveBuild = recursiveBuild;
		this.collect = collect;
	}

	List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}
	
	public Function<?, ?> getTargetFromPrerequisite() {
		return targetFromPrerequisite;
	}

	public boolean isRecursiveBuild() {
		return recursiveBuild;
	}

	BiFunction<?, List<?>, ?> getCollect() {
		return collect;
	}

	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
