package com.neaterbits.ide.component.common.language;

import com.neaterbits.ide.component.common.language.model.ParseableLanguage;

public interface LanguageComponent {

	Iterable<String> getFileSuffixes();
	
	LanguageName getLanguageName();
	
	LanguageStyling getStyling();
	
	ParseableLanguage getParseableLanguage();
	
}
