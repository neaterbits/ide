package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface EditorsView {

	EditorView displayFile(SourceFileResourcePath sourceFile);

	void closeFile(SourceFileResourcePath sourceFile);
	
	SourceFileResourcePath getCurrentEditedFile();
}
