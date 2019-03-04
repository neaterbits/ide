package com.neaterbits.ide.common.ui.view;

import java.util.Collection;

import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

public interface View {

	Collection<ActionContext> getActiveActionContexts();

	void addActionContextListener(ActionContextListener listener);
}
