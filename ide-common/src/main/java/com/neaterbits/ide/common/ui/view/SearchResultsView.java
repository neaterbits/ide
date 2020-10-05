package com.neaterbits.ide.common.ui.view;

import java.util.List;

import com.neaterbits.build.types.resource.SourceLineResourcePath;

public interface SearchResultsView extends View {

	void update(List<SourceLineResourcePath> sourceLines, SearchViewListener searchViewListener);
	
}
