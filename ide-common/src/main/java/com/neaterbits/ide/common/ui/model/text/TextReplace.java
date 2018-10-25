package com.neaterbits.ide.common.ui.model.text;

public final class TextReplace extends TextChange {

	private final String updatedText;
	
	public TextReplace(int startPos, int endPos, String changedText, String updatedText) {
		super(startPos, endPos, changedText);
		
		this.updatedText = updatedText;
	}

	public String getUpdatedText() {
		return updatedText;
	}

	@Override
	public int getChangeInNumberOfLines() {
		return    LinesFinder.getNumberOfNewlineChars(updatedText)
				- LinesFinder.getNumberOfNewlineChars(getChangedText());
	}
}

