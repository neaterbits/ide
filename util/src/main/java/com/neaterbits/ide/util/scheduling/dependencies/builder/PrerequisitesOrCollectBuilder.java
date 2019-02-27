package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.List;
import java.util.function.BiFunction;

public interface PrerequisitesOrCollectBuilder<
		CONTEXT extends TaskContext,
		TARGET,
		PRODUCT,
		ITEM>

	extends PrerequisitesBuilder<CONTEXT, TARGET> {

	PrerequisitesOrActionBuilder<CONTEXT, TARGET> collectToProduct(BiFunction<TARGET, List<ITEM>, PRODUCT> collect);
	
}
