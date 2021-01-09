package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.view.KeyEventListener;

public interface UIView extends UIDialogs {
	
	void setWindowTitle(String title);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void minMaxEditors();
}
