package com.neaterbits.ide.common.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;

public class CloseEditedAction extends Action {

	@Override
	public void execute(ActionExecuteParameters parameters) {

		parameters.getEditorsActions().closeCurrentEditedFile();
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}
}
