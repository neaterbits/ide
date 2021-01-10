package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public interface CompiledFileView extends View {

	void setSourceFileModel(SourceFileModel model);
	
	void onEditorCursorPosUpdate(long cursorOffset);
}
