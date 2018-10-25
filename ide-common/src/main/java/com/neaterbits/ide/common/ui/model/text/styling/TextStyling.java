package com.neaterbits.ide.common.ui.model.text.styling;

public class TextStyling {

	private final TextColor color;
	private final TextStyle style;
	
	public TextStyling(TextColor color, TextStyle style) {
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
