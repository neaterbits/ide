package com.neaterbits.ide.swt;

final class ReplaceText {
	private final int start;
	private final int length;
	private final String removedText;
	private final String addedText;
	
	ReplaceText(int start, int length, String removedText, String addedText) {
		this.start = start;
		this.length = length;
		this.removedText = removedText;
		this.addedText = addedText;
	}

	int getStart() {
		return start;
	}

	int getLength() {
		return length;
	}

	String getRemovedText() {
		return removedText;
	}

	String getAddedText() {
		return addedText;
	}
}
