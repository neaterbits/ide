package com.neaterbits.ide.common.ui.controller;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.model.common.SourceFileInfo;
import com.neaterbits.ide.common.model.source.SourceFilesModel;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.view.UIView;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.model.text.StringTextModel;
import com.neaterbits.ide.model.text.UnixLineDelimiter;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;

public final class EditUIController implements EditActions {

	private final UIView uiView;
	private final IDEComponents ideComponents;
	
	private final EditorsController editorsController;
	private final ProjectsController projectsController;
	private final CodeMapModel codeMapModel;
	
	EditUIController(
			UIViewAndSubViews uiView,
			ProjectsModel projectsModel,
			IDEComponents ideComponents,
			SourceFilesModel sourceFilesModel,
			CodeMapModel codeMapModel) {
		
		Objects.requireNonNull(uiView);
		Objects.requireNonNull(ideComponents);
		Objects.requireNonNull(codeMapModel);
		
		this.uiView = uiView;
		this.ideComponents = ideComponents;
		
		this.codeMapModel = codeMapModel;
		
		this.editorsController 	= new EditorsController(uiView.getEditorsView(), sourceFilesModel, uiView.getCompiledFileView());
		this.projectsController = new ProjectsController(projectsModel, uiView.getProjectView(), this);
	}

	SourceFileResourcePath getCurrentEditedFile() {
		return editorsController.getCurrentEditedFile();
	}
	
	void refreshProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();
		
		if (!currentEditedFile.getFile().exists()) {
			closeFile(currentEditedFile);
		}
		
		projectsController.refreshView();
	}
	
	@Override
	public void openSourceFileForEditing(SourceFileResourcePath sourceFilePath) {
	
		Objects.requireNonNull(sourceFilePath);
		
		final LanguageName languageName = ideComponents.getLanguages().detectLanguage(sourceFilePath);
		
		String text = null;
		try {
			text = IOUtil.readAll(sourceFilePath.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (text != null) {
			final StringTextModel textModel = new StringTextModel(UnixLineDelimiter.INSTANCE, text);
			
			uiView.setWindowTitle(makeTitle(sourceFilePath));
			
			final SourceFileInfo sourceFile = new SourceFileInfo(
					sourceFilePath,
					ideComponents.getLanguages().getLanguageComponent(languageName),
					codeMapModel);
			
			
			editorsController.displayFile(sourceFile, textModel);
		}
	}
	
	void showInProjectView(SourceFileResourcePath sourceFile, boolean setFocusInProjectView) {
		
		projectsController.showInProjectView(sourceFile, setFocusInProjectView);
	}
	
	private static String makeTitle(SourceFileResourcePath sourceFile) {
		return sourceFile.getModule().getName()
			 + '/' + PathUtil.removeDirectoryFromPath(sourceFile.getModule().getFile(), sourceFile.getFile());
	}

	@Override
	public void showCurrentEditedInProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			projectsController.showInProjectView(currentEditedFile, true);
		}
	}
	
	@Override
	public void closeCurrentEditedFile() {
		editorsController.closeCurrentEditedFile();
	}
	
	@Override
	public void minMaxEditors() {
		uiView.minMaxEditors();
	}

	void deleteResource(ResourcePath resourcePath) {

		// TODO do this based on change in project model instead
		if (Boolean.TRUE) {
			throw new UnsupportedOperationException();
		}
		
		Objects.requireNonNull(resourcePath);
		
		if (resourcePath instanceof SourceFileResourcePath) {
			final SourceFileResourcePath sourceFile = (SourceFileResourcePath)resourcePath;
		
			sourceFile.getFile().delete();
			
			final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

			if (sourceFile.equals(currentEditedFile)) {
				closeFile(sourceFile);
			}
		}

		projectsController.refreshView();
	}

	private void closeFile(SourceFileResourcePath sourceFile) {
		uiView.setWindowTitle("");
	}
}
