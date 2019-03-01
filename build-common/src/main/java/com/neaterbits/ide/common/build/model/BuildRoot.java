package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.ide.common.buildsystem.BuildSystemRootScan;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

public interface BuildRoot {

	File getPath();
	
	Collection<ModuleResourcePath> getModules();
	
	List<SourceFolderResourcePath> getSourceFolders(ModuleResourcePath module);
	
	void setSourceFolders(ModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);

	List<Dependency> getDependenciesForModule(ModuleResourcePath module);
	
	TargetDirectoryResourcePath getTargetDirectory(ModuleResourcePath module);
	
	CompiledModuleFileResourcePath getCompiledModuleFile(ModuleResourcePath module);

	void addListener(BuildRootListener listener);
	
	BuildSystemRootScan getBuildSystemRootScan();
	
	void downloadExternalDependencyAndAddToBuildModel(Dependency dependency);

	List<Dependency> getTransitiveExternalDependencies(Dependency dependency) throws ScanException;

	default <T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function) {
		
		for (ModuleResourcePath module : getModules()) {
			for (SourceFolderResourcePath sourceFolder : getSourceFolders(module)) {
				final T result = function.apply(sourceFolder);
				
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}
}
