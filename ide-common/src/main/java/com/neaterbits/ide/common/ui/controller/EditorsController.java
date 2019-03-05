package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;
import com.neaterbits.ide.model.text.TextModel;

final class EditorsController {

	private final EditorsView editorsView;
	private final Languages languages;
	
	EditorsController(EditorsView editorsView, Languages languages) {

		Objects.requireNonNull(editorsView);
		Objects.requireNonNull(languages);
		
		this.editorsView = editorsView;
		this.languages = languages;
	}

	void closeFile(SourceFileResourcePath sourceFile) {
		editorsView.closeFile(sourceFile);
	}
	
	void closeCurrentEditedFile() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			editorsView.closeFile(currentEditedFile);
		}
	}

	SourceFileResourcePath getCurrentEditedFile() {
		return editorsView.getCurrentEditedFile();
	}

	private static class EditorControllerDelegator implements EditorSourceActionContextProvider {
		private EditorController editorController;

		@Override
		public Collection<ActionContext> getActionContexts(long cursorOffset) {
		
			// Might be called while creating view so check for null
			return editorController != null
					? editorController.getActionContexts(cursorOffset)
					: null;
		}
	}
	
	EditorView displayFile(SourceFileResourcePath sourceFile, TextModel textModel, LanguageName language) {

		final LanguageComponent languageComonent = languages.getLanguageComponent(language);

		final EditorControllerDelegator editorControllerDelegator = new EditorControllerDelegator();
		
		
		final EditorView editorView = editorsView.displayFile(
				sourceFile,
				TextStylingHelper.makeTextStylingModel(languageComonent, textModel),
				editorControllerDelegator);
		
		final ParseableLanguage parseableLanguage = languageComonent.getParseableLanguage();
		
		final EditorController editorController = new EditorController(editorView, textModel, parseableLanguage);
		
		editorControllerDelegator.editorController = editorController;
		
		editorController.updateText();
		
		return editorView;
	}
}
