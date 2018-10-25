package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;

public interface EditorsView {

	EditorView displayFile(String fileName, BaseTextModel textModel);

	void closeFile();
}
