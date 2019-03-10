package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.compiler.util.model.ResolvedTypes;

public interface CodeMapModel extends ResolvedTypes {

	TypeSuggestions findSuggestions(String searchText, boolean onlyTypesWithSourceCode);
	
}

