package com.neaterbits.ide.common.ui.translation.texts;

import com.neaterbits.ide.common.ui.translation.EnumTranslateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;

public enum FindReplaceTexts implements EnumTranslateable<FindReplaceTexts> {

	FIND_REPLACE_TITLE,
	
	FIND_TEXT_LABEL,
	REPLACE_WITH_TEXT_LABEL,
	
	DIRECTION,
	FORWARD,
	BACKWARD,
	
	SCOPE,
	ALL,
	SELECTED_LINES,
	
	OPTIONS,
	CASE_SENSITIVE,
	WRAP_SEARCH,
	WHOLE_WORD,
	
	FIND_BUTTON,
	REPLACE_FIND_BUTTON,
	REPLACE_BUTTON,
	REPLACE_ALL_BUTTON,
	
	CLOSE_BUTTON
	;

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.FIND_REPLACE;
	}

	@Override
	public String getTranslationId() {
		return getTranslationId(this);
	}
}
