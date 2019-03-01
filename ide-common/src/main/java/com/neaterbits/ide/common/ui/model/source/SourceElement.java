package com.neaterbits.ide.common.ui.model.source;

public interface SourceElement extends SourceElementProperties {

	long getStartOffset();
	
	long getLength();
}
