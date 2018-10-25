package com.neaterbits.ide.common.resource;

public final class NamespaceResourcePath extends SourceFileHolderResourcePath {

	public NamespaceResourcePath(SourceFolderResourcePath sourceFolderPath, NamespaceResource namespaceResource) {
		super(sourceFolderPath, namespaceResource);
	}

	@Override
	public ResourcePath getParentPath() {
		return makeSourceFileHolderResourcePath();
	}
}
