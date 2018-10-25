package com.neaterbits.ide.common.resource;

import java.util.List;

public final class SourceFolderResourcePath extends SourceFileHolderResourcePath {

	public SourceFolderResourcePath(ModuleResourcePath moduleResourcePath, SourceFolderResource sourceFolderResource) {
		super(moduleResourcePath, sourceFolderResource);
	}

	public SourceFolderResource getSourceFolder() {
		return (SourceFolderResource)getLast();
	}

	@Override
	public ResourcePath getParentPath() {
		
		final List<ModuleResource> moduleResources = getPaths(1);

		return new ModuleResourcePath(moduleResources);
	}

	public String getName() {
		return getLast().getName();
	}
}
