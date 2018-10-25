package com.neaterbits.ide.common.ui.model.text;

public final class TextAdd extends TextEdit {

	private final String text;
	
	public TextAdd(int startPos, String text) {
		super(startPos);

		this.text = text;
	}

	public final String getAddedText() {
		return text;
	}

	@Override
	public int getChangeInNumberOfLines() {
		return LinesFinder.getNumberOfNewlineChars(text);
	}
}
