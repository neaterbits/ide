package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

public interface EditorsView {

	EditorView displayFile(SourceFileResourcePath sourceFile, TextStylingModel textStylingModel);

	void closeFile(SourceFileResourcePath sourceFile);
	
	SourceFileResourcePath getCurrentEditedFile();
}
