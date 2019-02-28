package com.neaterbits.ide.common.ui;

import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public interface UI<WINDOW> {

	ForwardToCaller getIOForwardToCaller();
	
	UIViewAndSubViews<WINDOW> makeUIView(ProjectsModel projectModel, TextEditorConfig config);
	
	void main();
	
}
