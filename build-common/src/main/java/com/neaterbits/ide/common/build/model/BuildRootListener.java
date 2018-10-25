package com.neaterbits.ide.common.build.model;

import com.neaterbits.ide.common.resource.ModuleResourcePath;

public interface BuildRootListener {
	
	void onSourceFoldersChanged(ModuleResourcePath module);

}
