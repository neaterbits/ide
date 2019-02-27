package com.neaterbits.ide.common.ui.view;

import java.util.Collection;

import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

public interface EditorView {

	String getText();
	
	void setCurrentText(String text);

	void applyStyles(Collection<TextStyleOffset> styles);
	
	void addTextChangeListener(TextChangeListener listener);
}
