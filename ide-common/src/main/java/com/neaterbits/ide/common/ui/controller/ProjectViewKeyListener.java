package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyMask;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.ProjectView;

final class ProjectViewKeyListener implements KeyEventListener {

	private final ProjectView projectView;
	private final EditUIController uiController;

	ProjectViewKeyListener(ProjectView projectView, EditUIController uiController) {
		
		Objects.requireNonNull(projectView);
		Objects.requireNonNull(uiController);

		this.projectView = projectView;
		this.uiController = uiController;
	}

	@Override
	public void onKeyPress(Key key, KeyMask mask) {

		if (key.getKeyCode() == 127) {
			
			final ResourcePath resourcePath = projectView.getSelected();
			
			if (resourcePath != null) {
				uiController.deleteResource(resourcePath);
			}
		}
		else if (key.getKeyCode() == Key.F5) {
			uiController.refreshProjectView();
		}
	}

	@Override
	public void onKeyRelease(Key key, KeyMask mask) {
		
	}
}
