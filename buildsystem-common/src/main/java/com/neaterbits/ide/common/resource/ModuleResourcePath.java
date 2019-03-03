package com.neaterbits.ide.common.resource;

import java.util.List;

public abstract class ModuleResourcePath extends FileSystemResourcePath {

	public ModuleResourcePath(List<? extends Resource> path) {
		super(path);
	}

	public ModuleResourcePath(Resource... resources) {
		super(resources);
	}
	
}
