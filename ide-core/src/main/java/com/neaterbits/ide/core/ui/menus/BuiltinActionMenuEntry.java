package com.neaterbits.ide.core.ui.menus;


import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.BuiltinAction;

public final class BuiltinActionMenuEntry
        extends BaseActionMenuEntry<
                        ActionApplicableParameters,
                        ActionExecuteParameters,
                        Action<ActionApplicableParameters, ActionExecuteParameters>> {

	private final BuiltinAction builtinAction;
	
	BuiltinActionMenuEntry(BuiltinAction builtinAction, KeyBindings keyBindings) {
	    
	    super(builtinAction.getAction(), keyBindings);

		this.builtinAction = builtinAction;
	}

	public BuiltinAction getAction() {
		return builtinAction;
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
