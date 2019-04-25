package com.neaterbits.ide.common.model.codemap;

import java.util.Map;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.TypeSources;

abstract class TypeSuggestionFinder {

	abstract boolean canRetrieveTypeVariant();
	
	abstract boolean hasSourceCode();
	
	abstract boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst);
	
	abstract boolean hasType(TypeName typeName, TypeSources typeSources);
}
