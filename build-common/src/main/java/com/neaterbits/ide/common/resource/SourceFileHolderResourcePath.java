package com.neaterbits.ide.common.resource;

public abstract class SourceFileHolderResourcePath extends SourcePath {
	public SourceFileHolderResourcePath(FileSystemResourcePath resourcePath) {
		super(resourcePath);
	}

	public SourceFileHolderResourcePath(FileSystemResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}
}
