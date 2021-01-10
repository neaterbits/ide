package com.neaterbits.ide.core.ui.view;

import com.neaterbits.build.types.resource.SourceLineResourcePath;

public interface SearchViewListener {

	void onSearchResultSelected(SourceLineResourcePath sourceLinePath);
	
}
