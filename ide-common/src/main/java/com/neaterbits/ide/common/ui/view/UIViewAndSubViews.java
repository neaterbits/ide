package com.neaterbits.ide.common.ui.view;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	BuildIssuesView getBuildIssuesView();
	
	SearchResultsView getSearchView();
}
