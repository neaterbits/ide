package com.neaterbits.ide.common.ui.model.text;

public interface TextBuilder {

	TextBuilder append(Text text);
	
	TextBuilder append(char c);
	
	Text toText();
}
