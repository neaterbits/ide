package com.neaterbits.ide.common.ui.model.dialogs;

import java.util.Collection;

public interface OpenTypeDialogModel {

	Collection<TypeSuggestion> getSuggestions(String searchText);
	
}
