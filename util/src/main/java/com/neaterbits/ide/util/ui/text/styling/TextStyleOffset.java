package com.neaterbits.ide.util.ui.text.styling;

import java.util.Objects;

public final class TextStyleOffset {

	private final int start;
	private final int length;
	private final TextColor color;
	
	public TextStyleOffset(int start, int length, TextColor color) {
		
		Objects.requireNonNull(color);
		
		this.start = start;
		this.length = length;
		this.color = color;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public TextColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "TextStyleOffset [start=" + start + ", length=" + length + ", color=" + color + "]";
	}
}
