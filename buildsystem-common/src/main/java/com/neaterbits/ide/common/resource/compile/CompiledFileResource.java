package com.neaterbits.ide.common.resource.compile;

import java.io.File;

import com.neaterbits.ide.common.resource.FileSystemResource;

// Compiled file, eg. .class file
public final class CompiledFileResource extends FileSystemResource {

	public CompiledFileResource(File file) {
		super(file);
	}
}
