package com.neaterbits.ide.common.ui.model.source;

public interface SourceFileModel {

	SourceElement getSourceTokenAt(long offset);
	
}
