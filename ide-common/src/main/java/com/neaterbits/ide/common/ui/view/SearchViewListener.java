package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceLineResourcePath;

public interface SearchViewListener {

	void onSearchResultSelected(SourceLineResourcePath sourceLinePath);
	
}
