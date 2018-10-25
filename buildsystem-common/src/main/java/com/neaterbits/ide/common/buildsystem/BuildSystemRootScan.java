package com.neaterbits.ide.common.buildsystem;

import java.util.List;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public interface BuildSystemRootScan {

	List<SourceFolderResourcePath> findSourceFolders(ModuleResourcePath moduleResourcePath);
	
}
