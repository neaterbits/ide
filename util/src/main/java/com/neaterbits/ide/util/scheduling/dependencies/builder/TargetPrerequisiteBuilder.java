package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.Function;

public interface TargetPrerequisiteBuilder<CONTEXT extends TaskContext, TARGET> {

	<PREREQUISITE>
	PrerequisiteFromBuilder<CONTEXT, TARGET> from(Function<TARGET, PREREQUISITE> from);
	
}
