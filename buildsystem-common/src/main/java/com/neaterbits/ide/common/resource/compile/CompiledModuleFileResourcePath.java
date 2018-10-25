package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.FileSystemResource;
import com.neaterbits.ide.common.resource.FileSystemResourcePath;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public final class CompiledModuleFileResourcePath extends FileSystemResourcePath {

	public CompiledModuleFileResourcePath(ModuleResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
