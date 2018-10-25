package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface ResultSubTargetBuilder<CONTEXT extends TaskContext, TARGET, RESULT> 
	extends SubTargetBuilder<CONTEXT, TARGET, ResultTypedPrerequisitesBuilder<CONTEXT,TARGET, RESULT>> {

}
