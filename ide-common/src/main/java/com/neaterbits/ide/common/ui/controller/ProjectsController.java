package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.view.ProjectView;
import com.neaterbits.ide.common.ui.view.ProjectViewListener;

public final class ProjectsController {

	private final ProjectView view;

	public ProjectsController(ProjectsModel model, ProjectView view, EditUIController uiController) {

		Objects.requireNonNull(model);
		Objects.requireNonNull(view);
		
		this.view = view;

		view.addListener(new ProjectViewListenerImpl(uiController));
		
		view.addKeyEventListener(new ProjectViewKeyListener(view, uiController));
}
	
	void showInProjectView(SourceFileResourcePath sourceFile, boolean setFocusInProjectView) {
		
		Objects.requireNonNull(sourceFile);
		
		view.showSourceFile(sourceFile, setFocusInProjectView);
	}

	void refreshView() {
		view.refresh();
	}

	private static class ProjectViewListenerImpl implements ProjectViewListener {

		private final EditUIController uiController;
		
		ProjectViewListenerImpl(EditUIController uiController) {
			this.uiController = uiController;
		}

		@Override
		public void onSourceFileSelected(SourceFileResourcePath sourceFile) {
			uiController.openSourceFileForEditing(sourceFile);
		}
	}
}
