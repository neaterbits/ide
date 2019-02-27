package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;

public interface TargetCollectIteratingBuilder<CONTEXT extends TaskContext, TARGET, PRODUCT, ITEM> {

	<PREREQUISITE>
	PrerequisiteCollectActionBuilder<CONTEXT, TARGET, PREREQUISITE, PRODUCT, ITEM>
		fromIterating(Constraint constraint, BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites);

}
