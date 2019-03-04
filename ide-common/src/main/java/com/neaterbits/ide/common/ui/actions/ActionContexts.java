package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

public interface ActionContexts {

	<T extends ActionContext> T getOfType(Class<T> cl);
	
}
