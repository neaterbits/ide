package com.neaterbits.ide.common.build.tasks;

import java.util.List;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.scheduling.task.TaskList;

final class FindSourceFoldersTaskList extends TaskList<BuildRoot, ModuleResourcePath, List<SourceFolderResourcePath>> {

	FindSourceFoldersTaskList(BuildRoot buildRoot) {
		super(
			buildRoot,
			BuildRoot::getModules,
			(br, module) -> br.getBuildSystemRootScan().findSourceFolders(module),
			(br, module, sourceFolders) -> br.setSourceFolders(module, sourceFolders));
	}
}
