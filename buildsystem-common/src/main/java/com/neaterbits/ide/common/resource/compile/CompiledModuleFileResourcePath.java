package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.FileSystemResource;
import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public final class CompiledModuleFileResourcePath extends FileSystemResourcePath {

	public CompiledModuleFileResourcePath(ProjectModuleResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
