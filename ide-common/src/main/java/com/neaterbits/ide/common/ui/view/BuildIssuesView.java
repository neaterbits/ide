package com.neaterbits.ide.common.ui.view;

import java.util.List;

import com.neaterbits.ide.common.build.model.compile.BuildIssue;

public interface BuildIssuesView extends View {

	void update(List<BuildIssue> buildIssues);
	
}
