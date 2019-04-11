package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.Collections;

import com.neaterbits.ide.common.ui.TextStyling;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

class TextStylingHelper {

	static TextStylingModel makeTextStylingModel(LanguageComponent languageComponent, TextModel textModel, SourceFileModel sourceFileModel) {
		
		final TextStyling textStyling = makeTextStyling(languageComponent);
		
		return new TextStylingModel() {
			
			@Override
			public Collection<TextStyleOffset> getLineStyleOffsets(long startPos, long length) {

				return makeStylesForLine(textModel, sourceFileModel, startPos, length, textStyling);
			}
		};
	}
	
	private static TextStyling makeTextStyling(LanguageComponent languageComponent) {
		
		TextStyling textStyling = null;
		
		if (languageComponent != null) {
			
			if (languageComponent != null && languageComponent.getStyling() != null) {

				textStyling = new LanguageTextStyling(languageComponent.getStyling());
				
			}
		}
		
		return textStyling;
	}
	
	
	private static Collection<TextStyleOffset> makeStylesForLine(
			TextModel textModel,
			SourceFileModel sourceFileModel,
			long lineStartOffset, long lineLength,
			TextStyling textStyling) {

		if (lineStartOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		final Collection<TextStyleOffset> styleOffsets;
		
		if (lineLength == 0) {
			styleOffsets = Collections.emptyList();
		}
		else {
		
			if (lineStartOffset + lineLength > textModel.getLength()) {
				throw new IllegalArgumentException("input length start " + lineStartOffset + " + length " + lineLength + " > " + textModel.getLength());
			}
			
			final long startLine = textModel.getLineAtOffset(lineStartOffset);
			final long endLine = textModel.getLineAtOffset(lineStartOffset + lineLength - 1);
			
			final long startOffset = textModel.getOffsetAtLine(startLine);
			final long endOffset = textModel.getOffsetAtLine(endLine) + textModel.getLineLengthWithoutAnyNewline(endLine);
	
			if (startOffset != lineStartOffset) {
				throw new IllegalStateException();
			}
			
			if (endOffset - startOffset != lineLength) {
				throw new IllegalStateException("line length mismatch " + (endOffset - startOffset) + "/" + lineLength);
			}
			
			final Text lineText = textModel.getTextRange(startOffset, lineLength);
			
			styleOffsets = textStyling.applyStylesToLine(startOffset, lineText, sourceFileModel);
		}
		
		return styleOffsets;
	}
}
