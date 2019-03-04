package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;

public abstract class MenuItemEntry extends MenuEntry {

	public abstract boolean isApplicableInContexts(
			ActionApplicableParameters parameters,
			ActionContexts focusedViewContexts,
			ActionContexts allContexts);
	
	public abstract void execute(ActionExecuteParameters parameters);
}
