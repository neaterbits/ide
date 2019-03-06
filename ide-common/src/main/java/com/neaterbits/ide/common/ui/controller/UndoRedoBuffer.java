package com.neaterbits.ide.common.ui.controller;

public interface UndoRedoBuffer {

	boolean hasUndoEntries();
	
	boolean hasRedoEntries();
}
