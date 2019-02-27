package com.neaterbits.ide.component.common.language;

public interface LanguageStyling {

	// For prefs interface
	Iterable<LanguageStyleable> getStylables();

	// Apply to some text chunk
	Iterable<LanguageStyleOffset> applyStyles(String text);
	
}
