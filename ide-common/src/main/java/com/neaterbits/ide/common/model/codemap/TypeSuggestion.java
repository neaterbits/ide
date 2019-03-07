package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface TypeSuggestion {

	TypeVariant getType();

	String getName();

	String getNamespace();

	SourceFileResourcePath getSourceFile();

}
