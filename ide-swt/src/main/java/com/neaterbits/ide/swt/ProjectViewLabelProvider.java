package com.neaterbits.ide.swt;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.neaterbits.ide.common.resource.ModuleResource;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.Resource;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

final class ProjectViewLabelProvider extends LabelProvider {

	private final Image projectImage = new Image(null, new File("icons/if_project_59298.png").getPath());
	private final Image sourceFolderImage = new Image(null, new File("icons/if_Noun_Project_100Icon_10px_grid-26_296917.png").getPath());
	private final Image namespaceImage = new Image(null, new File("icons/if_icon-139-package_314837.png").getPath());
	private final Image sourceFileImage = new Image(null, new File("icons/if_icon-78-document-file-java_315699.png").getPath());

	
	@Override
	public void dispose() {
		projectImage.dispose();
		sourceFolderImage.dispose();
		namespaceImage.dispose();
		sourceFileImage.dispose();
	}

	@Override
	public String getText(Object element) {
		
		final String text;
		
		if (element instanceof ModuleResourcePath) {
			
			final ModuleResourcePath moduleResourcePath = (ModuleResourcePath)element;
			
			final ModuleResource moduleResource = (ModuleResource)moduleResourcePath.getLast();
			
			text = moduleResource.getName();
		}
		else if (element instanceof ResourcePath) {
			
			final ResourcePath resourcePath = (ResourcePath)element;
			
			final Resource resource = resourcePath.getLast();
			
			text = resource.getName();
		}
		else {
			text = element.toString();
		}

		return text;
	}

	@Override
	public Image getImage(Object element) {
		
		final Image image;
		
		if (element instanceof ModuleResourcePath) {
			image = projectImage;
		}
		else if (element instanceof SourceFolderResourcePath) {
			image = sourceFolderImage;
		}
		else if (element instanceof NamespaceResourcePath) {
			image = namespaceImage;
		}
		else if (element instanceof SourceFileResourcePath) {
			image = sourceFileImage;
		}
		else {
			image = null;
		}
		
		return image;
	}
}
