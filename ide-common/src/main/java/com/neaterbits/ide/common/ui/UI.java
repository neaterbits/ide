package com.neaterbits.ide.common.ui;

import com.neaterbits.ide.common.ui.model.ProjectModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.UIView;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public interface UI<WINDOW> {

	ForwardToCaller getIOForwardToCaller();
	
	UIView<WINDOW> makeUIView(ProjectModel projectModel, TextEditorConfig config);
	
	void main();
	
}
