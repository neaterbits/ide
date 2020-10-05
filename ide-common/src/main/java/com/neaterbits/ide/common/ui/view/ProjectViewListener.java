package com.neaterbits.ide.common.ui.view;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface ProjectViewListener {

	void onSourceFileSelected(SourceFileResourcePath sourceFile);
	
}
