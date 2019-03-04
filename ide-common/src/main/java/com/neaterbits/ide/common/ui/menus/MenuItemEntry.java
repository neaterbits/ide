package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.actions.ActionContexts;

public abstract class MenuItemEntry extends MenuEntry {

	public abstract boolean isApplicableInContexts(ActionContexts focusedViewContexts, ActionContexts allContexts);
	
}
