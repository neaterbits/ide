package com.neaterbits.ide.common.ui.menus;

import java.util.Objects;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
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

	public boolean isApplicableInContexts(ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return action.getAction().isApplicableInContexts(focusedViewContexts, allContexts);
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
