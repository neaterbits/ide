package com.neaterbits.ide.common.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.component.common.language.LanguageStyleOffset;
import com.neaterbits.ide.component.common.language.LanguageStyleable;
import com.neaterbits.ide.component.common.language.LanguageStyling;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;

final class LanguageTextStyling implements TextStyling {
	
	private final LanguageStyling languageStyling;


	LanguageTextStyling(LanguageStyling languageStyling) {
		
		Objects.requireNonNull(languageStyling);
		
		this.languageStyling = languageStyling;
	}


	@Override
	public Collection<TextStyleOffset> applyStyles(Text text) {

		final List<TextStyleOffset> result = new ArrayList<>();
		
		for (LanguageStyleOffset offset : languageStyling.applyStyles(text)) {

			final LanguageStyleable styleable = offset.getStyleable();
			
			result.add(new TextStyleOffset(offset.getStart(), offset.getLength(), styleable.getDefaultColor()));
			
		}
		
		return result;
	}
}
