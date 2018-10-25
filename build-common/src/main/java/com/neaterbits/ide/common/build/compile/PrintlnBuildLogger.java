package com.neaterbits.ide.common.build.compile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public final class PrintlnBuildLogger implements BuildLogger {
	@Override
	public void onScanModuleSourceFolders(ModuleResourcePath module) {
		System.out.println("Scanning source folders for " + module.getName());
	}

	@Override
	public void onScanModuleSourceFoldersResult(ModuleResourcePath module,
			List<SourceFolderResourcePath> sourceFolders) {
		
		System.out.println("Got source folders for " + module.getName() + ": " + names(sourceFolders));
	}

	@Override
	public void onScanModuleSourceFilesResult(
			ModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt) {
		
		System.out.println("Got source files for " + module.getName() + ": "
					+ names(sourceFiles) + ", already built: " + names(alreadyBuilt));
		
	}
	
	@Override
	public void onBuildModules(Collection<ModuleResourcePath> modules) {
		System.out.println("Build modules: " + names(modules));
	}

	@Override
	public void onGetDependencies(ModuleResourcePath module) {
		System.out.println("Get dependencies for " + module.getName());
	}

	@Override
	public void onGetDependenciesResult(ModuleResourcePath module, List<Dependency> dependencies) {
		System.out.println("Got dependencies result for " + module.getName() + ": " + dependencies.stream()
						.map(dependency -> dependency.getResourcePath().getLast().getName())
						.collect(Collectors.toList()));
	}

	private static List<String> names(Collection<? extends ResourcePath> resourcePaths) {
		return resourcePaths.stream()
				.map(resourcePath -> resourcePath.getLast().getName())
				.collect(Collectors.toList());
	}
}
