package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;

public interface EditorsView {

	EditorView displayFile(SourceFileResourcePath sourceFile, BaseTextModel textModel);

	void closeFile(SourceFileResourcePath sourceFile);
	
	SourceFileResourcePath getCurrentEditedFile();
}
