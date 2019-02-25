package com.neaterbits.ide.common.ui.model.text;

import java.util.Objects;

public abstract class BaseTextModel {

	private final LineDelimiter lineDelimiter;
	
	public BaseTextModel(LineDelimiter lineDelimiter) {
		
		Objects.requireNonNull(lineDelimiter);
		
		this.lineDelimiter = lineDelimiter;
	}

	public abstract Text getText();
	
	public abstract void replaceTextRange(long start, long replaceLength, Text text);
	
	public abstract Text getTextRange(long start, long length);

	public abstract long getOffsetAtLine(long lineIndex);
	
	public final LineDelimiter getLineDelimiter() {
		return lineDelimiter;
	}
	
	public abstract long getLineCount();
	
	public abstract long getLineAtOffset(long offset);

	public abstract Text getLineWithoutAnyNewline(long lineIndex);
	public abstract Text getLineIncludingAnyNewline(long lineIndex);

	public abstract long getCharCount();
}
