package com.neaterbits.ide.common.ui.view;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface ProjectViewListener {

	void onSourceFileSelected(SourceFileResourcePath sourceFile);
	
}
