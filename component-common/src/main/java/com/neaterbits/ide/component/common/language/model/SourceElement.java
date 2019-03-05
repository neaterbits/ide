package com.neaterbits.ide.component.common.language.model;

public interface SourceElement extends ISourceTokenProperties {

	long getStartOffset();
	
	long getLength();
}
