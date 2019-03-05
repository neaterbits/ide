package com.neaterbits.ide.common.ui.actions.types.source;

import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.source.RenameContext;

public final class RenameAction extends RefactorAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts, ActionContexts allContexts) {
		return focusedContexts.hasOfType(RenameContext.class);
	}
}
