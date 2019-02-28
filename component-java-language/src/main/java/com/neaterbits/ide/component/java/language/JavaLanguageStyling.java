package com.neaterbits.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.component.common.language.RegexpLanguageStyleable;
import com.neaterbits.ide.component.common.language.RegexpLanguageStyling;
import com.neaterbits.ide.component.common.language.TokenType;
import com.neaterbits.ide.util.ui.text.styling.TextColor;

final class JavaLanguageStyling extends RegexpLanguageStyling {

	private static List<RegexpLanguageStyleable> STYLEABLES = Arrays.asList(
			
			new RegexpLanguageStyleable(
					TokenType.KEYWORD,
					new TextColor(0x80, 0x80, 0x50),
					" interface | class | if | for | while | do | throws | throw ")
			
	);
	
	
	JavaLanguageStyling() {
		super(STYLEABLES);
	}
}
