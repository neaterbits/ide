package com.neaterbits.ide.core.ui.controller;

import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.common.ui.controller.EditorsActions;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.core.ui.view.UIDialogs;
import com.neaterbits.util.threads.ForwardResultToCaller;

abstract class ActionExecuteState {

	private final IDEComponentsConstAccess components;
	private final UIDialogs uiDialogs;
	private final Clipboard clipboard;
	private final UndoRedoBuffer undoRedoBuffer;
	private final ComponentIDEAccess componentIDEAccess;
	private final BuildRoot buildRoot;
	private final EditorsActions editorsActions;
	private final CodeMapModel codeMap;
	private final ForwardResultToCaller forwardResultToCaller;
	
	private FindReplaceDialogModel findReplaceModel;

    abstract SourceFileResourcePath getCurrentSourceFileResourcePath();

    abstract SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath);

	ActionExecuteState(
			IDEComponentsConstAccess components,
			UIDialogs uiDialogs,
			Clipboard clipboard,
			UndoRedoBuffer undoRedoBuffer,
			ComponentIDEAccess componentIDEAccess,
			BuildRoot buildRoot,
			EditorsActions editorsActions,
			CodeMapModel codeMap,
			ForwardResultToCaller forwardResultToCaller) {
	
		Objects.requireNonNull(components);
		Objects.requireNonNull(uiDialogs);
		Objects.requireNonNull(clipboard);
		Objects.requireNonNull(undoRedoBuffer);
		Objects.requireNonNull(componentIDEAccess);
		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(editorsActions);
		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(forwardResultToCaller);
		
		this.components = components;
		this.uiDialogs = uiDialogs;
		this.clipboard = clipboard;
		this.undoRedoBuffer = undoRedoBuffer;
		this.componentIDEAccess = componentIDEAccess;
		this.buildRoot = buildRoot;
		this.editorsActions = editorsActions;
		this.codeMap = codeMap;
		this.forwardResultToCaller = forwardResultToCaller;
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

	UndoRedoBuffer getUndoRedoBuffer() {
		return undoRedoBuffer;
	}

	ComponentIDEAccess getComponentIDEAccess() {
		return componentIDEAccess;
	}

	BuildRoot getBuildRoot() {
		return buildRoot;
	}

	EditorsActions getEditorsActions() {
		return editorsActions;
	}

	CodeMapModel getCodeMap() {
		return codeMap;
	}

	ForwardResultToCaller getForwardResultToCaller() {
        return forwardResultToCaller;
    }

    FindReplaceDialogModel getFindReplaceModel() {
		return findReplaceModel;
	}

	void setFindReplaceModel(FindReplaceDialogModel findReplaceModel) {
		
		Objects.requireNonNull(findReplaceModel);
		
		this.findReplaceModel = findReplaceModel;
	}
}
