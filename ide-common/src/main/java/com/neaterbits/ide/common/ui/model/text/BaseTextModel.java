package com.neaterbits.ide.common.ui.model.text;

import java.util.Objects;

public abstract class BaseTextModel {

	private final String lineDelimiter;
	
	public BaseTextModel(String lineDelimiter) {
		
		Objects.requireNonNull(lineDelimiter);
		
		this.lineDelimiter = lineDelimiter;
	}

	public abstract String getText();
	
	public abstract void replaceTextRange(int start, int replaceLength, String text);
	
	public abstract String getTextRange(int start, int length);

	public abstract int getOffsetAtLine(int lineIndex);
	
	public final String getLineDelimiter() {
		return lineDelimiter;
	}
	
	public abstract int getLineCount();
	
	public abstract int getLineAtOffset(int offset);

	public abstract String getLine(int lineIndex);

	public abstract int getCharCount();

	public final int getLineLength(int lineIndex) {
		return getLine(lineIndex).length();
	}
}
