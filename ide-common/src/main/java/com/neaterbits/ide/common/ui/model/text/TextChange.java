package com.neaterbits.ide.common.ui.model.text;

import java.util.Objects;

public abstract class TextChange extends TextEdit {

	private final int endPos;
	private final String changedText;

	public TextChange(int startPos, int endPos, String changedText) {
		super(startPos);
		
		Objects.requireNonNull(changedText);
	
		this.endPos = endPos;
		this.changedText = changedText;
	}

	public final int getEndPos() {
		return endPos;
	}

	public final String getChangedText() {
		return changedText;
	}
}
