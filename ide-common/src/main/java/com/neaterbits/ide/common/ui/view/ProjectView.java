package com.neaterbits.ide.common.ui.view;

import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface ProjectView extends View {

	void refresh();
	
	void addListener(ProjectViewListener listener);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void showSourceFile(SourceFileResourcePath sourceFile, boolean setFocus);

	ResourcePath getSelected();
}
