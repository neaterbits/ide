package com.neaterbits.ide.util.dependencyresolution.builder;

import java.util.Collection;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface NoTargetIteratingBuilder<CONTEXT extends TaskContext> {
	
	/*
	<PREREQUISITE, TARGET>
	PrerequisiteActionBuilder<CONTEXT, TARGET, PREREQUISITE> iterating(BiFunction<CONTEXT, TARGET, Collection<PREREQUISITE>> getPrerequisites);
	*/

	<PREREQUISITE>
	NoTargetPrerequisiteBuilder<CONTEXT, PREREQUISITE> fromIterating(Function<CONTEXT, Collection<PREREQUISITE>> getPrerequisites);

}
