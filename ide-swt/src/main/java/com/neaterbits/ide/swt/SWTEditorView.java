package com.neaterbits.ide.swt;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorView;

abstract class SWTEditorView extends SWTView implements EditorView {

	SWTEditorView() {
		
	}

	abstract void setSelectedAndFocused();

	abstract void close();
	
	abstract void configure(TextEditorConfig config);

	abstract SourceFileResourcePath getSourceFile();
	
}
