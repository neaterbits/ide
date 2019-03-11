package com.neaterbits.ide.common.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.EditorContext;
import com.neaterbits.ide.common.ui.view.EditorView;

public final class SelectAllAction extends Action {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		final EditorView editorView = (EditorView)parameters.getFocusedView();
		
		editorView.selectAll();
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {
	
		return focusedContexts.hasOfType(EditorContext.class);
	}
}
