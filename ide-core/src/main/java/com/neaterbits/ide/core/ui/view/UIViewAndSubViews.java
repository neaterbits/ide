package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.view.ViewList;
import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;
import com.neaterbits.ide.component.common.ui.ComponentDialogContext;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	ViewList getViewList();

	ComponentDialogContext getComponentDialogContext();
	
	ComponentCompositeContext getComponentCompositeContext();
}
