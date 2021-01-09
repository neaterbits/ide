package com.neaterbits.ide.core.ui.controller;

import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.component.common.action.ActionComponentAppParameters;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;

class ActionApplicableParametersImpl
        extends ActionComponentAppParameters
        implements ActionApplicableParameters {

	private final ActionExecuteState executeState;

	ActionApplicableParametersImpl(ActionExecuteState executeState, Languages languages) {
	    
	    super(executeState.getBuildRoot(), languages);
		
		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
	}

	@Override
    public final SourceFileResourcePath getCurrentSourceFileResourcePath() {
        return executeState.getCurrentSourceFileResourcePath();
    }

    @Override
    public final SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
        return executeState.getSourceFileModel(sourceFileResourcePath);
    }

    @Override
	public final Clipboard getClipboard() {
		return executeState.getClipboard();
	}

	@Override
	public final UndoRedoBuffer getUndoRedoBuffer() {
		return executeState.getUndoRedoBuffer();
	}

	@Override
	public final FindReplaceDialogModel getFindReplaceModel() {
		return executeState.getFindReplaceModel();
	}
}
