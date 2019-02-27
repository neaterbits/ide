package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;

public interface TargetIteratingBuilder<CONTEXT extends TaskContext, TARGET> {

	<PREREQUISITE>
	PrerequisiteActionBuilder<CONTEXT, TARGET, PREREQUISITE> fromIterating(Constraint constraint, BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites);

}
