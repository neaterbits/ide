package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;

public interface EditorView extends View {

	Text getText();
	
	void setCurrentText(Text text);

	void addTextChangeListener(TextEditorChangeListener listener);

	void addCursorPositionListener(CursorPositionListener cursorPositionListener);
	
	long getCursorPosition();

	void setCursorPosition(long offset);
	
	TextRange getSelection();
	
	void addDisposeListener(ViewDisposeListener listener);
	
	void selectAll();
	
	void select(long offset, long length);

	void triggerStylingRefresh();
}
