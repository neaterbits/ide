package com.neaterbits.ide.common.ui.model.text;

public final class TextRemove extends TextChange {

	public TextRemove(int startPos, int endPos, String changedText) {
		super(startPos, endPos, changedText);
	}

	@Override
	public int getChangeInNumberOfLines() {
		return LinesFinder.getNumberOfNewlineChars(getChangedText());
	}
}
