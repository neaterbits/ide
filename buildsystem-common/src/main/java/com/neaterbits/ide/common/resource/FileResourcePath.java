package com.neaterbits.ide.common.resource;

// a file (not directory) on disk
public abstract class FileResourcePath extends FileSystemResourcePath {

	public FileResourcePath(DirectoryResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}
}
