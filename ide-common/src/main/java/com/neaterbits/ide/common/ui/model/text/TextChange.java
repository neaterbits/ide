package com.neaterbits.ide.common.ui.model.text;

import java.util.Objects;

import com.neaterbits.ide.util.ui.text.Text;

public abstract class TextChange extends TextEdit {

	private final long length;
	private final Text changedText;

	public TextChange(long startPos, long length, Text changedText) {
		super(startPos);
		
		Objects.requireNonNull(changedText);
	
		this.length = length;
		this.changedText = changedText;
	}

	@Override
	public final long getOldLength() {
		return length;
	}

	@Override
	public Text getOldText() {
		return changedText;
	}
}
