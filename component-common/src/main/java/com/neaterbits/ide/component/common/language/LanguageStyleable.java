package com.neaterbits.ide.component.common.language;

import java.util.Objects;

import com.neaterbits.ide.util.ui.text.styling.TextColor;

public abstract class LanguageStyleable {

	private final TokenType tokenType;
	private final TextColor defaultColor;

	public LanguageStyleable(TokenType tokenType, TextColor defaultColor) {
		
		Objects.requireNonNull(tokenType);
		
		this.tokenType = tokenType;
		this.defaultColor = defaultColor;
	}

	public final TokenType getTokenType() {
		return tokenType;
	}

	public final TextColor getDefaultColor() {
		return defaultColor;
	}
}
