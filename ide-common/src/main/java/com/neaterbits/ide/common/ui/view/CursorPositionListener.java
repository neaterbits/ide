package com.neaterbits.ide.common.ui.view;

@FunctionalInterface
public interface CursorPositionListener {

	void onCursorPositionChanged(long cursorPos);
}
