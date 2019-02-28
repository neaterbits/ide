package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.util.ui.text.Text;

public final class EditorController {

	private final EditorView editorView;
	private BaseTextModel textModel;
	
	public EditorController(EditorView editorView, BaseTextModel textModel) {

		Objects.requireNonNull(editorView);
		
		this.editorView = editorView;
		this.textModel = textModel;
	
		editorView.addTextChangeListener((start, length, newText) -> {

			this.textModel.replaceTextRange(start, length, newText);
			
		});
	}

	
	public void updateText() {
		
		final Text newText = textModel.getText();
		
		editorView.setCurrentText(newText);
		
	}
	
	public void setTextModel(BaseTextModel textModel) {
		
		Objects.requireNonNull(textModel);
		
		// System.out.println("## setText: " + textModel.getText());
		
		this.textModel = textModel;

		updateText();
	}
}
