package com.neaterbits.ide.common.ui.controller;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface EditActions {
	
	void openSourceFileForEditing(SourceFileResourcePath sourceFile);
	
	void showCurrentEditedInProjectView();

	void closeCurrentEditedFile();

	void minMaxEditors();
}
