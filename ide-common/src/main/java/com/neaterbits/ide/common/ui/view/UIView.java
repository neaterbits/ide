package com.neaterbits.ide.common.ui.view;

public interface UIView<WINDOW> extends UIDialogs<WINDOW> {
	
	void setWindowTitle(String title);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void minMaxEditors();
}
