package com.neaterbits.ide.common.ui.menus;

import java.util.Objects;

import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.BuiltinAction;

public final class BuiltinActionMenuEntry extends MenuItemEntry {

	private final BuiltinAction action;

	BuiltinActionMenuEntry(BuiltinAction action) {

		Objects.requireNonNull(action);
		
		this.action = action;
	}

	public BuiltinAction getAction() {
		return action;
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return action.getAction().isApplicableInContexts(parameters, focusedViewContexts, allContexts);
	}

	@Override
	public void execute(ActionExecuteParameters parameters) {
		action.getAction().execute(parameters);
	}

	@Override
	public String getTranslationNamespace() {
		return action.getTranslationNamespace();
	}

	@Override
	public String getTranslationId() {
		return action.getTranslationId();
	}

	@Override
	public String toString() {
		return "BuiltinActionMenuEntry [action=" + action + "]";
	}
}
