package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.util.ui.text.styling.TextStyling;

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
		
		final EditorView editorView = editorsView.displayFile(sourceFile);
		
		final EditorController editorController = new EditorController(editorView, textModel, makeTextStyling(language));
		
		editorController.updateText();
		
		return editorView;
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
	
	
	
}
