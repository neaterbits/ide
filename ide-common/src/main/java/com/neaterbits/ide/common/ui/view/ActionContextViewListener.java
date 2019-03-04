package com.neaterbits.ide.common.ui.view;

import java.util.Collection;

import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

public interface ActionContextViewListener {

	void onUpdated(View view, Collection<ActionContext> updatedContexts);

}
