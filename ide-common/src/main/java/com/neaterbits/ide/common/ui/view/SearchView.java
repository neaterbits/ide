package com.neaterbits.ide.common.ui.view;

import java.util.List;

import com.neaterbits.ide.common.resource.SourceLineResourcePath;

public interface SearchView {

	void update(List<SourceLineResourcePath> sourceLines, SearchViewListener searchViewListener);
	
}
