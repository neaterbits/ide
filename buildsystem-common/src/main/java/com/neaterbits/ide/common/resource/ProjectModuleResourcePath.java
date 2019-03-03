package com.neaterbits.ide.common.resource;

import java.util.List;

public class ProjectModuleResourcePath extends ModuleResourcePath {

	public ProjectModuleResourcePath(List<ModuleResource> path) {
		super(path);
	}

	public ProjectModuleResourcePath(ModuleResource... resources) {
		super(resources);
	}

	public boolean isDirectSubModuleOf(ProjectModuleResourcePath other) {
		return isDirectSubPathOf(other);
	}

	@Override
	public ResourcePath getParentPath() {
		
		System.out.println("getParentPath: " + this);
		
		final ResourcePath parentPath = new ProjectModuleResourcePath(getPaths(1));

		System.out.println("getParentPath, return " + parentPath);
		
		return parentPath;
	}

	public final String getName() {
		final ModuleResource moduleResource = (ModuleResource)getLast();

		return moduleResource.getName();
	}
}
