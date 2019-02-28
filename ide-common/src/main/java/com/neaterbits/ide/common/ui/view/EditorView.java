package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.util.ui.text.Text;

public interface EditorView {

	Text getText();
	
	void setCurrentText(Text text);

	void addTextChangeListener(TextChangeListener listener);
}
