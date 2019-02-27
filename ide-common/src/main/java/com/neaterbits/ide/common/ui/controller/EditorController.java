package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;
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
			int start, int length,
			String textToStyle,
			TextStyling textStyling) {
		
		final int startLine = textModel.getLineAtOffset(start);
		final int endLine = textModel.getLineAtOffset(start + length - 1);
		
		final int startOffset = textModel.getOffsetAtLine(startLine);
		
		final int endOffset = textModel.getOffsetAtLine(endLine) + textModel.getLineLength(endLine);
		
		final String text = textModel.getTextRange(startOffset, endOffset);
		
		final Collection<TextStyleOffset> stylesOffsets = textStyling.applyStyles(text);

		System.out.println("## apply styles " + stylesOffsets + " from " + textStyling);
		
		editorView.applyStyles(stylesOffsets);
	}
	
	public void updateText() {
		
		final String newText = textModel.getText();
		
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
