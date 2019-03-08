package com.neaterbits.ide.component.common.language.model;

import com.neaterbits.compiler.common.model.ResolvedTypes;

public interface ParseableLanguage {

	SourceFileModel parse(String string, ResolvedTypes resolvedTypes);
	
}
