package com.neaterbits.ide.common.ui.controller;

import com.neaterbits.ide.common.ui.model.dialogs.SearchDirection;
import com.neaterbits.ide.common.ui.model.dialogs.SearchScope;
import com.neaterbits.ide.util.ui.text.Text;

public interface EditorActions {

	long find(long pos, Text searchText, SearchDirection direction, SearchScope scope, boolean caseSensitive, boolean wrapSearch, boolean wholeWord);
	
	void replace(long pos, long replaceLength, Text replacement);
	
	void selectAll();
	
}
