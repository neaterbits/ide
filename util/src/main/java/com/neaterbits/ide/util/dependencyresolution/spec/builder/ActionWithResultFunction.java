package com.neaterbits.ide.util.dependencyresolution.spec.builder;

public interface ActionWithResultFunction<CONTEXT, TARGET, RESULT> {

	ActionResult<RESULT> perform(CONTEXT context, TARGET target);
	
}