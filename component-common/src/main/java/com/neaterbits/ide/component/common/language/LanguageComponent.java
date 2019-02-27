package com.neaterbits.ide.component.common.language;

public interface LanguageComponent {

	Iterable<String> getFileSuffixes();
	
	LanguageName getLanguageName();
	
	LanguageStyling getStyling();
	
}
