package com.neaterbits.ide.common.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;

public class MinMaxEditorsAction extends Action {

	@Override
	public void execute(ActionExecuteParameters parameters) {

		parameters.getEditActions().minMaxEditors();
	}

	@Override
	public boolean isApplicableInContexts(ActionContexts contexts) {
		return true;
	}
}
