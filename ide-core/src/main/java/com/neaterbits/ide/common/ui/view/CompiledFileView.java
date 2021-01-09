package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public interface CompiledFileView extends View {

	void setSourceFileModel(SourceFileModel model);
	
	void onEditorCursorPosUpdate(long cursorOffset);
}
