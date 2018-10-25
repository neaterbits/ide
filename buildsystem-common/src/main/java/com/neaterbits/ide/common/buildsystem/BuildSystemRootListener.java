package com.neaterbits.ide.common.buildsystem;

import com.neaterbits.ide.common.resource.ModuleResourcePath;

public interface BuildSystemRootListener {

	void onSourceFoldersChanged(ModuleResourcePath module);

}
