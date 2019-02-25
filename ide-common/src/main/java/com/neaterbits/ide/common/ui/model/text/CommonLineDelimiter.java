package com.neaterbits.ide.common.ui.model.text;

public class CommonLineDelimiter extends LineDelimiter {

	@Override
	public int getNumberOfNewlineCharsForOneLineShift(Text text, long offset) {

		final char c = text.charAt(offset);
		
		int newlineChars = 0;
		
		if (c == '\r') {
			newlineChars = 1;
			
			if (offset < text.length() - 1 && text.charAt(offset + 1) == '\n') {
				++ newlineChars;
			}
		}
		else if (c == '\n') {
			newlineChars = 1;
		}

		return newlineChars;
	}
}
