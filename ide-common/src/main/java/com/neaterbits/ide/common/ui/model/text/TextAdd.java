package com.neaterbits.ide.common.ui.model.text;

import java.util.Objects;

public final class TextAdd extends TextEdit {

	private final Text text;
	
	public TextAdd(long startPos, Text text) {
		super(startPos);

		Objects.requireNonNull(text);
		
		this.text = text;
	}

	public final Text getAddedText() {
		return text;
	}

	@Override
	public Text getOldText() {
		return Text.EMPTY_TEXT;
	}

	@Override
	public long getOldLength() {
		return 0;
	}
	
	@Override
	public Text getNewText() {
		return text;
	}

	@Override
	public long getNewLength() {
		return text.length();
	}

	@Override
	public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return getChangeInNumberOfLines(text, lineDelimiter);
	}

	@Override
	public String toString() {
		return "TextAdd [text=" + text + "]";
	}
}
