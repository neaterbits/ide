package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface TypeSuggestion {

	TypeVariant getType();

	String getNamespace();

	String getName();

	String getBinaryName(); // eg. SomeType.class
	
	SourceFileResourcePath getSourceFile();

	default boolean isSourceFile() {
		return getSourceFile() != null;
	}
}
