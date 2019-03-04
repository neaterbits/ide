package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.ui.view.UIDialogs;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;

final class ActionExecuteState {

	private final IDEComponentsConstAccess components;
	private final UIDialogs uiDialogs;
	private final Clipboard clipboard;
	private final ComponentIDEAccess componentIDEAccess;
	private final BuildRoot buildRoot;
	private final EditActions editActions;

	ActionExecuteState(
			IDEComponentsConstAccess components,
			UIDialogs uiDialogs,
			Clipboard clipboard,
			ComponentIDEAccess componentIDEAccess,
			BuildRoot buildRoot,
			EditActions editActions) {
	
		Objects.requireNonNull(components);
		Objects.requireNonNull(uiDialogs);
		Objects.requireNonNull(clipboard);
		Objects.requireNonNull(componentIDEAccess);
		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(editActions);
		
		this.components = components;
		this.uiDialogs = uiDialogs;
		this.clipboard = clipboard;
		this.componentIDEAccess = componentIDEAccess;
		this.buildRoot = buildRoot;
		this.editActions = editActions;
		
	}

	IDEComponentsConstAccess getComponents() {
		return components;
	}

	UIDialogs getUIDialogs() {
		return uiDialogs;
	}

	Clipboard getClipboard() {
		return clipboard;
	}

	ComponentIDEAccess getComponentIDEAccess() {
		return componentIDEAccess;
	}

	public BuildRoot getBuildRoot() {
		return buildRoot;
	}

	public EditActions getEditActions() {
		return editActions;
	}
}
