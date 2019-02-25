package com.neaterbits.ide.common.ui.model.text;

public abstract class LineDelimiter {

	public abstract int getNumberOfNewlineCharsForOneLineShift(Text text, long offset);
	
}
