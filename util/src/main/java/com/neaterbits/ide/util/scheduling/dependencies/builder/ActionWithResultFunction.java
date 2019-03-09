package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface ActionWithResultFunction<CONTEXT, TARGET, RESULT> {

	ActionResult<RESULT> perform(CONTEXT context, TARGET target);
	
}
