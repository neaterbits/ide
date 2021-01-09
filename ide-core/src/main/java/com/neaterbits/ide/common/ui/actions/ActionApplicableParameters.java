package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.ui.controller.UndoRedoBuffer;
import com.neaterbits.ide.common.ui.model.dialogs.FindReplaceDialogModel;

public interface ActionApplicableParameters extends ActionAppParameters {

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();

	FindReplaceDialogModel getFindReplaceModel();
}
