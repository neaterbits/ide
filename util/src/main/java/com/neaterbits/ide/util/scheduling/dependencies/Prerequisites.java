package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

final class Prerequisites {

	private final List<Prerequisite<?>> prerequisites;
	private final BiFunction<?, List<?>, ?> collect;

	Prerequisites(List<Prerequisite<?>> prerequisites, BiFunction<?, List<?>, ?> collect) {
		
		Objects.requireNonNull(prerequisites);
		
		this.prerequisites = prerequisites;
		this.collect = collect;
	}

	List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}

	BiFunction<?, List<?>, ?> getCollect() {
		return collect;
	}

	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
