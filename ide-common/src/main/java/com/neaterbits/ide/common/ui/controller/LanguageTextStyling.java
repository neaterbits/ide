package com.neaterbits.ide.common.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.component.common.language.LanguageStyleOffset;
import com.neaterbits.ide.component.common.language.LanguageStyleable;
import com.neaterbits.ide.component.common.language.LanguageStyling;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextColor;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;

final class LanguageTextStyling implements TextStyling {
	
	private final LanguageStyling languageStyling;


	LanguageTextStyling(LanguageStyling languageStyling) {
		
		Objects.requireNonNull(languageStyling);
		
		this.languageStyling = languageStyling;
	}


	@Override
	public Collection<TextStyleOffset> applyStylesToLine(long startPos, Text lineText) {

		if (lineText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		final List<TextStyleOffset> result = new ArrayList<>();
		
		long lastOffset = 0;
		long lastLength = 0;
		
		final Iterable<LanguageStyleOffset> offsets = languageStyling.applyStyles(lineText);

		final TextColor defaultColor = new TextColor(0, 0, 0);
		
		for (LanguageStyleOffset offset : offsets) {

			final LanguageStyleable styleable = offset.getStyleable();
			
			if (offset.getStart() > lastOffset + lastLength) {
				
				result.add(new TextStyleOffset(
						startPos + lastOffset,
						offset.getStart() - (lastOffset + lastLength),
						defaultColor));
			}
			
			result.add(new TextStyleOffset(startPos + offset.getStart(), offset.getLength(), styleable.getDefaultColor()));
			
			lastOffset = offset.getStart();
			lastLength = offset.getLength();
		}
		
		if (lineText.length() > lastOffset + lastLength) {
			result.add(new TextStyleOffset(
					lastOffset,
					lineText.length() - (lastOffset + lastLength),
					defaultColor));
		}
		
		return result;
	}
}
