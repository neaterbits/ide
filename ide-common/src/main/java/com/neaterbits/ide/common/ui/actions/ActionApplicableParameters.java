package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.ui.controller.UndoRedoBuffer;

public interface ActionApplicableParameters {

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();
}
