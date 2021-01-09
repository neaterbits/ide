package com.neaterbits.ide.core.ui.menus;

import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionAppParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExeParameters;
import com.neaterbits.ide.common.ui.actions.ActionExecutionException;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.keys.KeyCombination;
import com.neaterbits.ide.common.ui.menus.MenuItemEntry;

abstract class BaseActionMenuEntry<
            ACTION_APP_PARAMETERS extends ActionAppParameters,
            ACTION_EXE_PARAMETERS extends ActionExeParameters,
            ACTION extends Action<ACTION_APP_PARAMETERS, ACTION_EXE_PARAMETERS>>

    extends MenuItemEntry<ACTION_APP_PARAMETERS, ACTION_EXE_PARAMETERS> {

    final ACTION action;
    private final KeyCombination keyCombination;

    BaseActionMenuEntry(ACTION action, KeyBindings keyBindings) {

        this.action = action;
        this.keyCombination = keyBindings.findKeyCombination(action);
    }

    @Override
    public final KeyCombination getKeyCombination() {
        return keyCombination;
    }

    @Override
    public final boolean isApplicableInContexts(ACTION_APP_PARAMETERS parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
        
        return action.isApplicableInContexts(parameters, focusedViewContexts, allContexts);
    }

    @Override
    public final void execute(ACTION_EXE_PARAMETERS parameters) throws ActionExecutionException {
        
        action.execute(parameters);
    }
}
