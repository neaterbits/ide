package com.neaterbits.ide.common.ui.actions;

public abstract class Action {

	public abstract void execute(ActionExecuteParameters parameters);
	
	public abstract boolean isApplicableInContexts(ActionContexts focusedContexts, ActionContexts allContexts);
	
}
