package com.neaterbits.ide.common.ui.controller;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.model.text.StringTextModel;
import com.neaterbits.ide.common.ui.model.text.UnixLineDelimiter;
import com.neaterbits.ide.common.ui.view.UIView;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;

public final class EditUIController implements EditActions {

	private final UIView uiView;
	private final IDEComponents ideComponents;
	
	private final EditorsController editorsController;
	private final ProjectsController projectsController;
	
	EditUIController(
			UIViewAndSubViews uiView,
			ProjectsModel projectsModel,
			IDEComponents ideComponents) {
		
		Objects.requireNonNull(uiView);
		Objects.requireNonNull(ideComponents);
		
		this.uiView = uiView;
		this.ideComponents = ideComponents;
		
		this.editorsController 	= new EditorsController(uiView.getEditorsView(), ideComponents.getLanguages());
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
	public void openSourceFileForEditing(SourceFileResourcePath sourceFile) {
	
		Objects.requireNonNull(sourceFile);
		
		String text = null;
		try {
			text = IOUtil.readAll(sourceFile.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (text != null) {
			final StringTextModel textModel = new StringTextModel(UnixLineDelimiter.INSTANCE, text);
			
			uiView.setWindowTitle(makeTitle(sourceFile));
			
			editorsController.displayFile(sourceFile, textModel, ideComponents.getLanguages().detectLanguage(sourceFile));
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
