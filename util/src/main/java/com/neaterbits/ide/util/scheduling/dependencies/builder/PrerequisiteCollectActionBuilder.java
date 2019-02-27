package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface PrerequisiteCollectActionBuilder<CONTEXT extends TaskContext, TARGET, PREREQUISITE, PRODUCT, ITEM> {

	PrerequisitesOrCollectBuilder<CONTEXT, TARGET, PRODUCT, ITEM>
	buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);

	PrerequisitesBuilder<CONTEXT, TARGET> collectToProduct(BiFunction<TARGET, List<ITEM>, PRODUCT> collect);
}
