package com.neaterbits.ide.common.ui.view;

import java.util.Collection;

import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

public interface EditorView {

	Text getText();
	
	void setCurrentText(Text text);

	void applyStyles(Collection<TextStyleOffset> styles);
	
	void addTextChangeListener(TextChangeListener listener);
}
