package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

public final class EditorController {

	private final EditorView editorView;
	private BaseTextModel textModel;
	private final TextStyling textStyling;
	
	public EditorController(EditorView editorView, BaseTextModel textModel, TextStyling textStyling) {

		Objects.requireNonNull(editorView);
		
		this.editorView = editorView;
		this.textModel = textModel;
		this.textStyling = textStyling;
	
		editorView.addTextChangeListener((start, length, newText) -> {

			this.textModel.replaceTextRange(start, length, newText);
			
			applyStyles(editorView, textModel, start, length, newText, textStyling);
		});
	}

	private static void applyStyles(
			EditorView editorView,
			BaseTextModel textModel,
			long start, long length,
			Text textToStyle,
			TextStyling textStyling) {

		if (start < 0) {
			throw new IllegalArgumentException();
		}
		
		if (length == 0) {
			throw new IllegalArgumentException();
		}
		
		if (start + length > textModel.getLength()) {
			throw new IllegalArgumentException("input length start " + start + " + length " + length + " > " + textModel.getLength());
		}
		
		final long startLine = textModel.getLineAtOffset(start);
		final long endLine = textModel.getLineAtOffset(start + length - 1);
		
		final long startOffset = textModel.getOffsetAtLine(startLine);
		
		final long endOffset = textModel.getOffsetAtLine(endLine) + textModel.getLineLengthWithoutAnyNewline(endLine);
		
		final Text text = textModel.getTextRange(startOffset, endOffset);
		
		final Collection<TextStyleOffset> stylesOffsets = textStyling.applyStyles(text);

		System.out.println("## apply styles " + stylesOffsets + " from " + textStyling);
		
		editorView.applyStyles(stylesOffsets);
	}
	
	public void updateText() {
		
		final Text newText = textModel.getText();
		
		editorView.setCurrentText(newText);
		
		applyStyles(editorView, textModel, 0, newText.length(), newText, textStyling);
	}
	
	public void setTextModel(BaseTextModel textModel) {
		
		Objects.requireNonNull(textModel);
		
		// System.out.println("## setText: " + textModel.getText());
		
		this.textModel = textModel;

		updateText();
	}
}
