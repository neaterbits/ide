package com.neaterbits.ide.core.ui.view;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	BuildIssuesView getBuildIssuesView();
	
	SearchResultsView getSearchView();
	
	CompiledFileView getCompiledFileView();
	
	ViewList getViewList();

}
