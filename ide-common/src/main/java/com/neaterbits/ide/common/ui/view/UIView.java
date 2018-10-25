package com.neaterbits.ide.common.ui.view;

public interface UIView<WINDOW> extends UIDialogs<WINDOW> {
	
	void setWindowTitle(String title);
	
	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	BuildIssuesView getBuildIssuesView();
	
	SearchView getSearchView();

	void addKeyEventListener(KeyEventListener keyEventListener);

	void minMaxEditors();
}
