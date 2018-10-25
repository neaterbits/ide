package com.neaterbits.ide.common.resource;

public final class LibraryResourcePath extends ResourcePath {

	public LibraryResourcePath(LibraryResource resource) {
		super(resource);
	}

	@Override
	public ResourcePath getParentPath() {
		return null;
	}
}
