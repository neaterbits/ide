package com.neaterbits.ide.common.resource;

public final class LibraryResourcePath extends ModuleResourcePath {

	public LibraryResourcePath(LibraryResource resource) {
		super(resource);
	}

	@Override
	public ResourcePath getParentPath() {
		return null;
	}

	@Override
	public String getName() {
		return getFile().getName();
	}
}
