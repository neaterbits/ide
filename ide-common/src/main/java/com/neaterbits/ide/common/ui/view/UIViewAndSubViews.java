package com.neaterbits.ide.common.ui.view;

public interface UIViewAndSubViews<WINDOW> extends UIView<WINDOW> {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	BuildIssuesView getBuildIssuesView();
	
	SearchView getSearchView();

}
