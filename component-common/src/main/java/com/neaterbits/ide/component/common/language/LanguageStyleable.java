package com.neaterbits.ide.component.common.language;

import java.util.Objects;

import com.neaterbits.ide.util.ui.text.styling.TextColor;

public class LanguageStyleable {

	public static final LanguageStyleable KEYWORD_DEFAULT =
			new LanguageStyleable(SyntaxHighlightingTokenType.KEYWORD, new TextColor(0x80, 0x80, 0x50));
	
	private final SyntaxHighlightingTokenType tokenType;
	private final TextColor defaultColor;

	public LanguageStyleable(SyntaxHighlightingTokenType tokenType, TextColor defaultColor) {
		
		Objects.requireNonNull(tokenType);
		
		this.tokenType = tokenType;
		this.defaultColor = defaultColor;
	}

	public final SyntaxHighlightingTokenType getTokenType() {
		return tokenType;
	}

	public final TextColor getDefaultColor() {
		return defaultColor;
	}
}
