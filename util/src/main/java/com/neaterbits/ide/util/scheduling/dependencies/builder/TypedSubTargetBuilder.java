package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface TypedSubTargetBuilder<CONTEXT extends TaskContext, TARGET> 
	extends SubTargetBuilder<CONTEXT, TARGET, PrerequisitesOrActionBuilder<CONTEXT,TARGET>> {

}
