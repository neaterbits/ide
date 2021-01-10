package com.neaterbits.ide.core.ui.view;

import java.util.Collection;

import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.view.View;

public interface ActionContextViewListener {

	void onUpdated(View view, Collection<ActionContext> updatedContexts);

}
