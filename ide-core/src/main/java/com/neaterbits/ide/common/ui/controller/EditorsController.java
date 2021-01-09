package com.neaterbits.ide.common.ui.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.common.SourceFileInfo;
import com.neaterbits.ide.common.model.source.SourceFilesModel;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.CompiledFileView;
import com.neaterbits.ide.common.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

final class EditorsController {

	private final EditorsView editorsView;
	private final TextEditorConfig config;
	private final SourceFilesModel sourceFilesModel;
	private final CompiledFileView compiledFileView;
	
	private final Map<SourceFileResourcePath, EditorController> editorControllers;
	
	EditorsController(EditorsView editorsView, TextEditorConfig config, SourceFilesModel sourceFilesModel, CompiledFileView compiledFileView) {

		Objects.requireNonNull(editorsView);
		Objects.requireNonNull(config);
		Objects.requireNonNull(sourceFilesModel);
		
		this.editorsView = editorsView;
		this.config = config;
		this.sourceFilesModel = sourceFilesModel;
		this.compiledFileView = compiledFileView;
		
		this.editorControllers = new HashMap<>();
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

	EditorActions getCurrentEditor() {
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		return currentEditedFile != null ? editorControllers.get(currentEditedFile) : null;
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
	
	EditorView displayFile(SourceFileInfo sourceFile, TextModel textModel) {

		final EditorControllerDelegator editorControllerDelegator = new EditorControllerDelegator();
		
		final DelegatingSourceFileModel delegatingSourceFileModel = new DelegatingSourceFileModel();

		final TextStylingModel textStylingModel = TextStylingHelper.makeTextStylingModel(sourceFile.getLanguage(), delegatingSourceFileModel);
		
		final EditorView editorView = editorsView.displayFile(sourceFile.getPath(), textStylingModel, editorControllerDelegator);
		
		final EditorController editorController = new EditorController(
				editorView,
				config,
				compiledFileView,
				textModel,
				sourceFilesModel,
				sourceFile,
				delegatingSourceFileModel);
		
		editorControllerDelegator.editorController = editorController;
		
		editorController.updateText();
		
		editorControllers.put(sourceFile.getPath(), editorController);
		
		editorView.addDisposeListener(() -> editorControllers.remove(sourceFile.getPath()));

		return editorView;
	}
}
