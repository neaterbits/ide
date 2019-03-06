package com.neaterbits.ide.common.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;

public final class UndoAction extends Action {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
	}

	@Override
	public boolean isApplicableInContexts(
			ActionApplicableParameters parameters,
			ActionContexts focusedContexts,
			ActionContexts allContexts) {

		return parameters.getUndoRedoBuffer().hasUndoEntries();
	}
}
