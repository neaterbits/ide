package com.neaterbits.ide.common.model.codemap;

import java.util.Map;

import com.neaterbits.compiler.common.TypeName;

abstract class TypeSuggestionFinder {

	abstract boolean canRetrieveTypeVariant();
	
	abstract boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst);
	
}
