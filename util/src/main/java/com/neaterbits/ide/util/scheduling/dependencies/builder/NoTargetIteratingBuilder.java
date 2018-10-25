package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.Collection;
import java.util.function.Function;

public interface NoTargetIteratingBuilder<CONTEXT extends TaskContext> {
	
	/*
	<PREREQUISITE, TARGET>
	PrerequisiteActionBuilder<CONTEXT, TARGET, PREREQUISITE> iterating(BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites);
	*/

	<PREREQUISITE>
	NoTargetPrerequisiteBuilder<CONTEXT, PREREQUISITE> iterating(Function<CONTEXT, Collection<PREREQUISITE>> getPrerequisites);

}
