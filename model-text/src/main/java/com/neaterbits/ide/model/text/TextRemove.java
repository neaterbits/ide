package com.neaterbits.ide.model.text;

import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.Text;

public final class TextRemove extends TextChange {

	public TextRemove(long startPos, long length, Text changedText) {
		super(startPos, length, changedText);
	}
	
	@Override
	public Text getNewText() {
		return Text.EMPTY_TEXT;
	}

	@Override
	public long getNewLength() {
		return 0;
	}

	@Override
	public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return getChangeInNumberOfLines(getOldText(), lineDelimiter);
	}
}
