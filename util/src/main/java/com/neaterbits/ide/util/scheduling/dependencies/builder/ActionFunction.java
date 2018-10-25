package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface ActionFunction<CONTEXT, TARGET> {

	void perform(CONTEXT context, TARGET target, ActionParameters actionParameters) throws Exception;
	
}
