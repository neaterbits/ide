package com.neaterbits.ide.common.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.common.model.ISourceToken;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.actions.contexts.source.SourceTokenContext;
import com.neaterbits.ide.common.ui.view.CompiledFileView;
import com.neaterbits.ide.common.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.component.common.language.model.ISourceTokenProperties;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.util.ui.text.Text;

public final class EditorController implements EditorSourceActionContextProvider {

	private final EditorView editorView;
	private TextModel textModel;
	
	private SourceFileModel sourceFileModel;
	
	public EditorController(EditorView editorView, CompiledFileView compiledFileView, TextModel textModel, ParseableLanguage parseableLanguage) {

		Objects.requireNonNull(editorView);
		
		this.editorView = editorView;
		this.textModel = textModel;

		updateSourceFileModel(textModel, compiledFileView, parseableLanguage);
		
		editorView.addTextChangeListener((start, length, newText) -> {

			this.textModel.replaceTextRange(start, length, newText);

			updateSourceFileModel(textModel, compiledFileView, parseableLanguage);
		});
		
		if (compiledFileView != null) {
			editorView.addCursorPositionListener(compiledFileView::onEditorCursorPosUpdate);
		}
	}
	
	private void updateSourceFileModel(TextModel textModel, CompiledFileView compiledFileView, ParseableLanguage parseableLanguage) {
		this.sourceFileModel = parseableLanguage.parse(textModel.getText().asString());
		
		if (compiledFileView != null) {
			compiledFileView.setSourceFileModel(sourceFileModel);
		}
	}
	
	@Override
	public Collection<ActionContext> getActionContexts(long cursorOffset) {
		
		// System.out.println("## get contexts for cursor " + cursorOffset);
		
		final Collection<ActionContext> actionContexts;
		
		if (sourceFileModel == null) {
			actionContexts = null;
		}
		else {
			
			final ISourceToken sourceToken = sourceFileModel.getSourceTokenAt(cursorOffset);
	
			// System.out.println("## got source token " + sourceToken);
			
			if (sourceToken != null) {
				actionContexts = new ArrayList<>();

				final ISourceTokenProperties tokenProperties = SourceFileModel.getProperties(sourceToken);
				
				actionContexts.add(new SourceTokenContext(sourceToken, tokenProperties));
			}
			else {
				actionContexts = null;
			}
		}
		
		return actionContexts;
	}

	public void updateText() {
		
		final Text newText = textModel.getText();
		
		editorView.setCurrentText(newText);
		
	}
	
	public void setTextModel(TextModel textModel) {
		
		Objects.requireNonNull(textModel);
		
		// System.out.println("## setText: " + textModel.getText());
		
		this.textModel = textModel;

		updateText();
	}
}
