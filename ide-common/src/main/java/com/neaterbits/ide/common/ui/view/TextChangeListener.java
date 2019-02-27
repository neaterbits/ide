package com.neaterbits.ide.common.ui.view;

@FunctionalInterface
public interface TextChangeListener {

	void onTextChange(int start, int length, String updatedText);
	
}
