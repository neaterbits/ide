package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

public final class EditorsController {

	private final EditorsView editorsView;
	private final Languages languages;
	
	public EditorsController(EditorsView editorsView, Languages languages) {

		Objects.requireNonNull(editorsView);
		Objects.requireNonNull(languages);
		
		this.editorsView = editorsView;
		this.languages = languages;
	}

	public EditorView displayFile(SourceFileResourcePath sourceFile, BaseTextModel textModel, LanguageName language) {
		
		final EditorView editorView = editorsView.displayFile(sourceFile, makeTextStylingModel(language, textModel));
		
		final EditorController editorController = new EditorController(editorView, textModel);
		
		editorController.updateText();
		
		return editorView;
	}

	private TextStylingModel makeTextStylingModel(LanguageName language, BaseTextModel textModel) {
	
		final TextStyling textStyling = makeTextStyling(language);
		
		return new TextStylingModel() {
			
			@Override
			public Collection<TextStyleOffset> getStyleOffsets(long startPos, long length) {

				return makeStylesForLine(textModel, startPos, length, textStyling);
			}
		};
	}
	
	private TextStyling makeTextStyling(LanguageName language) {
		
		TextStyling textStyling = null;
		
		if (language != null) {
			final LanguageComponent languageComponent = languages.getLanguageComponent(language);
			
			if (languageComponent != null && languageComponent.getStyling() != null) {

				textStyling = new LanguageTextStyling(languageComponent.getStyling());
				
			}
		}
		
		return textStyling;
	}
	
	
	private static Collection<TextStyleOffset> makeStylesForLine(
			BaseTextModel textModel,
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
			
			styleOffsets = textStyling.applyStylesToLine(startOffset, lineText);
		}
		
		return styleOffsets;
	}
}
