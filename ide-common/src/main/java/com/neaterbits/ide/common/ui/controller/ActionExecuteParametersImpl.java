package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.view.UIDialogs;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;

final class ActionExecuteParametersImpl implements ActionExecuteParameters {

	private final ActionExecuteState executeState;
	private final SourceFileResourcePath currentEditedFile;
	
	ActionExecuteParametersImpl(ActionExecuteState executeState, SourceFileResourcePath currentEditedFile) {

		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
		this.currentEditedFile = currentEditedFile;
	}

	@Override
	public IDEComponentsConstAccess getComponents() {
		return executeState.getComponents();
	}

	@Override
	public SourceFileResourcePath getCurrentEditedFile() {
		return currentEditedFile;
	}

	@Override
	public UIDialogs getUIDialogs() {
		return executeState.getUIDialogs();
	}

	@Override
	public ComponentIDEAccess getComponentIDEAccess() {
		return executeState.getComponentIDEAccess();
	}

	@Override
	public BuildRoot getBuildRoot() {
		return executeState.getBuildRoot();
	}

	@Override
	public EditActions getEditActions() {
		return executeState.getEditActions();
	}
}
