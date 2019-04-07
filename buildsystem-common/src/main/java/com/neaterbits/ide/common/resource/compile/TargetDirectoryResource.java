package com.neaterbits.ide.common.resource.compile;

import java.io.File;

import com.neaterbits.ide.common.resource.FileSystemResource;

// Represents the root output directory for build artifacts
public final class TargetDirectoryResource extends FileSystemResource {

	public TargetDirectoryResource(File file) {
		super(file);
	}
}
