package com.neaterbits.ide.component.common.language;

import java.util.Objects;

public final class LanguageStyleOffset {

	private final int start;
	private final int length;
	private final LanguageStyleable styleable;
	
	public LanguageStyleOffset(int start, int length, LanguageStyleable styleable) {

		Objects.requireNonNull(styleable);
		
		this.start = start;
		this.length = length;
		this.styleable = styleable;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public LanguageStyleable getStyleable() {
		return styleable;
	}
}
