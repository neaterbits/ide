package com.neaterbits.ide.core.ui.view;

import java.util.List;

import com.neaterbits.build.types.compile.BuildIssue;
import com.neaterbits.ide.common.ui.view.View;

public interface BuildIssuesView extends View {

	void update(List<BuildIssue> buildIssues);
	
}
