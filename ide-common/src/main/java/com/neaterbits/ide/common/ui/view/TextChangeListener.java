package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.util.ui.text.Text;

@FunctionalInterface
public interface TextChangeListener {

	void onTextChange(long start, long length, Text updatedText);
	
}
