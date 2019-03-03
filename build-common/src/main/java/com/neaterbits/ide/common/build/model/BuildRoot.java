package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.ide.common.buildsystem.BuildSystemRootScan;
import com.neaterbits.ide.common.buildsystem.Scope;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

public interface BuildRoot {

	File getPath();
	
	Collection<ProjectModuleResourcePath> getModules();
	
	List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module);
	
	void setSourceFolders(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);

	List<Dependency> getDependenciesForProjectModule(ProjectModuleResourcePath module);

	List<Dependency> getDependenciesForExternalLibrary(Dependency dependency, Scope scope, boolean includeOptionalDependencies);

	TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module);
	
	CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module);

	void addListener(BuildRootListener listener);
	
	BuildSystemRootScan getBuildSystemRootScan();
	
	void downloadExternalDependencyAndAddToBuildModel(Dependency dependency);

	Scope getDependencyScope(Dependency dependency);
	
	default <T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function) {
		
		for (ProjectModuleResourcePath module : getModules()) {
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
