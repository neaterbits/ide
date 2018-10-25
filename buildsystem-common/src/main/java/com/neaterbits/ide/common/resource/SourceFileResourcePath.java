package com.neaterbits.ide.common.resource;

import java.io.File;

public final class SourceFileResourcePath extends SourcePath {

	public SourceFileResourcePath(SourceFileHolderResourcePath sourceHolderPath, SourceFileResource sourceFileResource) {
		super(sourceHolderPath, sourceFileResource);
	}
	
	@Override
	public ResourcePath getParentPath() {
		return makeSourceFileHolderResourcePath();
	}
	
	public File getSourceFolder() {

		for (int i = 1; i < length(); ++ i) {
			final Resource resource = getFromLast(i);
			
			if (resource instanceof SourceFolderResource) {
				return ((SourceFolderResource)resource).getFile();
			}
		}

		return null;
	}

	public String getName() {
		return getLast().getName();
	}
}
