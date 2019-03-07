package com.neaterbits.ide.common.model.codemap;


public interface CodeMapModel {

	TypeSuggestions findSuggestions(String searchText, boolean onlyTypesWithSourceCode);
	
}

