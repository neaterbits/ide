package com.neaterbits.ide.common.resource;

public final class LibraryResourcePath extends FileSystemResourcePath {

	public LibraryResourcePath(LibraryResource resource) {
		super(resource);
	}

	@Override
	public ResourcePath getParentPath() {
		return null;
	}
}
