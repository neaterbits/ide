package com.neaterbits.ide.common.ui;

import com.neaterbits.ide.common.ui.controller.UIParameters;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public interface UI {

	ForwardToCaller getIOForwardToCaller();
	
	UIViewAndSubViews makeUIView(UIParameters uiParameters, ProjectsModel projectModel);
	
	void main();
	
	void addFocusListener(ViewFocusListener focusListener);
	
}
