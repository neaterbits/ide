package com.neaterbits.ide.common.build.compile;

import java.util.Collection;
import java.util.List;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public interface BuildLogger {

	void onScanModuleSourceFolders(ModuleResourcePath module);
	
	void onScanModuleSourceFoldersResult(ModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);
	
	void onScanModuleSourceFilesResult(
			ModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt);
	
	void onBuildModules(Collection<ModuleResourcePath> modules);
	
	
	void onGetDependencies(ModuleResourcePath module);

	void onGetDependenciesResult(ModuleResourcePath module, List<Dependency> dependencies);
}
