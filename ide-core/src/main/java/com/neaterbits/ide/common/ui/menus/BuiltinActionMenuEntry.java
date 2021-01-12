package com.neaterbits.ide.common.ui.menus;

import java.util.Objects;

import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.BuiltinAction;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.keys.KeyCombination;

public final class BuiltinActionMenuEntry
        extends MenuItemEntry<ActionApplicableParameters, ActionExecuteParameters> {

	private final BuiltinAction builtinAction;
	private final KeyCombination keyCombination;
	
	BuiltinActionMenuEntry(BuiltinAction builtinAction, KeyBindings keyBindings) {

		Objects.requireNonNull(builtinAction);
		
		this.builtinAction = builtinAction;
		this.keyCombination = keyBindings.findKeyCombination(builtinAction.getAction());
	}

	public BuiltinAction getAction() {
		return builtinAction;
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return builtinAction.getAction().isApplicableInContexts(parameters, focusedViewContexts, allContexts);
	}

	@Override
	public KeyCombination getKeyCombination() {
		return keyCombination;
	}

	@Override
	public void execute(ActionExecuteParameters parameters) {
		builtinAction.getAction().execute(parameters);
	}

	@Override
	public String getTranslationNamespace() {
		return builtinAction.getTranslationNamespace();
	}

	@Override
	public String getTranslationId() {
		return builtinAction.getTranslationId();
	}

	@Override
	public String toString() {
		return "BuiltinActionMenuEntry [action=" + builtinAction + "]";
	}
}
