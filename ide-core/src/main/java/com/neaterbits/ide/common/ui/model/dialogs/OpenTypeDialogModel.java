package com.neaterbits.ide.common.ui.model.dialogs;

import com.neaterbits.ide.common.model.codemap.TypeSuggestions;

public interface OpenTypeDialogModel {

	TypeSuggestions getSuggestions(String searchText);
	
}
