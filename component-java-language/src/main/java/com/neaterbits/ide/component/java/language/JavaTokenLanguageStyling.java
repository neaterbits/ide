package com.neaterbits.ide.component.java.language;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.ide.component.common.language.LanguageStyleOffset;
import com.neaterbits.ide.component.common.language.LanguageStyleable;
import com.neaterbits.ide.component.common.language.LanguageStyling;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.util.ui.text.Text;

final class JavaTokenLanguageStyling implements LanguageStyling {

	@Override
	public Iterable<LanguageStyleable> getStylables() {
		return null;
	}

	@Override
	public Iterable<LanguageStyleOffset> applyStylesToLine(long lineOffset, Text text, SourceFileModel sourceFileModel) {

		final List<LanguageStyleOffset> offsets = new ArrayList<>();
		
		final long lineLength = text.length();
		
		sourceFileModel.iterate(lineOffset, lineLength, token -> {
			
			final LanguageStyleable styleable;
			
			switch (token.getTokenType()) {
			case KEYWORD:
				styleable = LanguageStyleable.KEYWORD_DEFAULT;
				
				offsets.add(new LanguageStyleOffset(token.getStartOffset(), token.getLength(), styleable));
				break;
				
			default:
				break;
			}
		});
		
		
		return offsets;
	}
}
