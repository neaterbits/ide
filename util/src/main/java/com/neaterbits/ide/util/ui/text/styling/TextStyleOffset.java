package com.neaterbits.ide.util.ui.text.styling;


public final class TextStyleOffset {

	public static final TextColor SELECTED_BG_COLOR = new TextColor(0x60, 0x60, 0xD0);
	
	private final long start;
	private final long length;
	private final TextColor color;
	private final TextColor bgColor;
	
	public TextStyleOffset(long start, long length, TextColor color) {
		this(start, length, color, null);
	}

	public TextStyleOffset(long start, long length, TextColor color, TextColor bgColor) {

		this.start = start;
		this.length = length;
		this.color = color;
		this.bgColor = bgColor;
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
	
	public TextColor getBgColor() {
		return bgColor;
	}

	@Override
	public String toString() {
		return "TextStyleOffset [start=" + start + ", length=" + length + ", color=" + color + "]";
	}
}
