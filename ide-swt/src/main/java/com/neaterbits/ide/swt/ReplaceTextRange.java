package com.neaterbits.ide.swt;

class ReplaceTextRange {

	private final int start;
	private final int length;
	private final String text;

	ReplaceTextRange(int start, int length, String text) {
		this.start = start;
		this.length = length;
		this.text = text;
	}

	int getStart() {
		return start;
	}
	
	int getLength() {
		return length;
	}
	
	String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "ReplaceTextRange [start=" + start + ", length=" + length + ", text=" + text + "]";
	}
}
