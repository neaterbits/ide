package com.neaterbits.ide.common.ui.actions.types.clipboard;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.EditorSelectionContext;

public final class CopyAction extends ClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isApplicableInContexts(ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return focusedViewContexts.hasOfType(EditorSelectionContext.class);
	}
}
