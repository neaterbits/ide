package com.neaterbits.ide.common.ui.model.text;

public abstract class TextEdit {

	private final int startPos;

	public abstract int getChangeInNumberOfLines();

	public TextEdit(int startPos) {
		this.startPos = startPos;
	}

	public final int getStartPos() {
		return startPos;
	}
}
