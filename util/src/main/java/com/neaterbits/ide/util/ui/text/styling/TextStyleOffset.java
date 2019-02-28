package com.neaterbits.ide.util.ui.text.styling;

import java.util.Objects;

public final class TextStyleOffset {

	private final long start;
	private final long length;
	private final TextColor color;
	
	public TextStyleOffset(long start, long length, TextColor color) {
		
		Objects.requireNonNull(color);
		
		this.start = start;
		this.length = length;
		this.color = color;
	}

	public long getStart() {
		return start;
	}

	public long getLength() {
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
