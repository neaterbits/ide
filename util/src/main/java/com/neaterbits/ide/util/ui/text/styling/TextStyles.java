package com.neaterbits.ide.util.ui.text.styling;

public class TextStyles {

	private final TextColor color;
	private final TextStyle style;
	
	public TextStyles(TextColor color, TextStyle style) {
		this.color = color;
		this.style = style;
	}

	public TextColor getColor() {
		return color;
	}

	public TextStyle getStyle() {
		return style;
	}
}
