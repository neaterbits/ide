package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.translation.Translateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;

public enum BuiltinMenu implements Translateable {

	FILE,
	EDIT,
	SOURCE,
	REFACTOR,
	SEARCH;

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.MENUES;
	}

	@Override
	public String getTranslationId() {
		return name().toLowerCase();
	}
}
