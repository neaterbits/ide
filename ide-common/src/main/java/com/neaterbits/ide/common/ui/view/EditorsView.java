package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

public interface EditorsView extends View {

	EditorView displayFile(SourceFileResourcePath sourceFile, TextStylingModel textStylingModel, EditorSourceActionContextProvider editorSourceActionContextProvider);

	void closeFile(SourceFileResourcePath sourceFile);
	
	SourceFileResourcePath getCurrentEditedFile();
}
