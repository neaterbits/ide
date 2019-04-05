package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.util.List;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisitesOrCollectBuilder<
		CONTEXT extends TaskContext,
		TARGET,
		PRODUCT,
		ITEM>

	extends PrerequisitesBuilder<CONTEXT, TARGET> {

	PrerequisitesOrActionBuilder<CONTEXT, TARGET> collectSubTargetsToProduct(BiFunction<TARGET, List<ITEM>, PRODUCT> collect);

	PrerequisitesOrActionBuilder<CONTEXT, TARGET> collectSubProductsToProduct(BiFunction<TARGET, List<ITEM>, PRODUCT> collect);

}
