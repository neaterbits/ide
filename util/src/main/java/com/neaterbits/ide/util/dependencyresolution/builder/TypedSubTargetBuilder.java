package com.neaterbits.ide.util.dependencyresolution.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface TypedSubTargetBuilder<CONTEXT extends TaskContext, TARGET> 
	extends SubTargetBuilder<CONTEXT, TARGET, PrerequisitesOrActionBuilder<CONTEXT,TARGET>> {

}
