package com.neaterbits.ide.component.common.language;

import com.neaterbits.ide.util.ui.text.Text;

public interface LanguageStyling {

	// For prefs interface
	Iterable<LanguageStyleable> getStylables();

	// Apply to some text chunk
	Iterable<LanguageStyleOffset> applyStyles(Text text);
	
}
