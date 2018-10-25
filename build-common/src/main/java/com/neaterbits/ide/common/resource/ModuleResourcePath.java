package com.neaterbits.ide.common.resource;

import java.util.List;

public class ModuleResourcePath extends FileSystemResourcePath {

	public ModuleResourcePath(List<ModuleResource> path) {
		super(path);
	}

	public ModuleResourcePath(ModuleResource... resources) {
		super(resources);
	}

	public boolean isDirectSubModuleOf(ModuleResourcePath other) {
		return isDirectSubPathOf(other);
	}

	@Override
	public ResourcePath getParentPath() {
		return new ModuleResourcePath(getPaths(1));
	}

	public final String getName() {
		final ModuleResource moduleResource = (ModuleResource)getLast();

		return moduleResource.getName();
	}
}
