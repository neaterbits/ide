package com.neaterbits.ide.swt;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.util.swt.TreeContentAdapter;

final class ProjectViewContentProvider extends TreeContentAdapter implements ITreeContentProvider {

	private final ProjectsModel projectModel;
	
	public ProjectViewContentProvider(ProjectsModel projectModel) {
		
		Objects.requireNonNull(projectModel);
		
		this.projectModel = projectModel;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		viewer.refresh();

	}
	
	@Override
	public Object getParent(Object element) {
		
		final ResourcePath resourcePath = (ResourcePath)element;
		
		System.out.println("## getParent: " + element);

		final ResourcePath parentPath = resourcePath.isAtRoot() ? null : resourcePath.getParentPath();
		
		System.out.println("## getParent, result " + parentPath);
		
		return parentPath;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {

		final Object [] elements;
		
		if (inputElement instanceof ProjectsModel) {
			elements = new Object [] { projectModel.getRoot() };
		}
		else {
		
			final ResourcePath resourcePath = (ResourcePath)inputElement;
			
			final List<ResourcePath> resourcePaths = projectModel.getResources(resourcePath);
			
			elements = resourcePaths.toArray(new ResourcePath[resourcePaths.size()]);
		}

		return elements;
	}
}
