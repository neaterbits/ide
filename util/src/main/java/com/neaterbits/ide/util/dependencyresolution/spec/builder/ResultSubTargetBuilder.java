package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface ResultSubTargetBuilder<CONTEXT extends TaskContext, TARGET, RESULT> 
	extends SubTargetBuilder<CONTEXT, TARGET, ResultTypedPrerequisitesBuilder<CONTEXT,TARGET, RESULT>> {

}
