package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public final class TargetDirectoryResourcePath extends FileSystemResourcePath {

	public TargetDirectoryResourcePath(ModuleResourcePath moduleResourcePath, TargetDirectoryResource targetDirectoryResource) {
		super(moduleResourcePath, targetDirectoryResource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
