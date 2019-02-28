package com.neaterbits.ide.common.resource;

import java.util.ArrayList;
import java.util.List;

public abstract class SourcePath extends FileSystemResourcePath {

	
	SourcePath(FileSystemResourcePath resourcePath) {
		super(resourcePath);
	}

	SourcePath(FileSystemResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}

	public final ModuleResourcePath getModule() {
		
		final List<ModuleResource> moduleResources = new ArrayList<>();
		
		for (int i = 0; i < length(); ++ i) {
			final Resource resource = get(i);
			
			if (resource instanceof ModuleResource) {
				moduleResources.add((ModuleResource)resource);
			}
			else {
				break;
			}
		}
		
		return new ModuleResourcePath(moduleResources);
	}

	
	final SourceFileHolderResourcePath makeSourceFileHolderResourcePath() {
		final Resource resource = getFromLast(1);
		
		final SourceFileHolderResourcePath resourcePath;
		
		if (resource instanceof NamespaceResource) {

			final SourceFolderResourcePath sourceFolderResourcePath = makeSourceFolderResourcePath(2);

			resourcePath = new NamespaceResourcePath(sourceFolderResourcePath, (NamespaceResource)resource);
		}
		else if (resource instanceof SourceFolderResource) {
			
			resourcePath = makeSourceFolderResourcePath(1);
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return resourcePath;
	}

	protected final SourceFolderResourcePath makeSourceFolderResourcePath(int subtractLength) {

		final List<ModuleResource> moduleResources = getPaths(subtractLength + 1);
		
		return new SourceFolderResourcePath(new ModuleResourcePath(moduleResources), (SourceFolderResource)getFromLast(subtractLength));
	}
}
