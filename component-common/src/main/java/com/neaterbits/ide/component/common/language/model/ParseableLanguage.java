package com.neaterbits.ide.component.common.language.model;

import com.neaterbits.compiler.util.model.ResolvedTypes;

public interface ParseableLanguage {

	SourceFileModel parse(String string, ResolvedTypes resolvedTypes);
	
}
