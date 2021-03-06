package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreePathViewerSorter;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.model.ProjectModelListener;
import com.neaterbits.ide.common.ui.view.ActionContextListener;
import com.neaterbits.ide.core.ui.view.KeyEventListener;
import com.neaterbits.ide.core.ui.view.ProjectView;
import com.neaterbits.ide.core.ui.view.ProjectViewListener;
import com.neaterbits.ide.ui.swt.SWTView;
import com.neaterbits.ide.ui.swt.SWTViewList;

final class SWTProjectView extends SWTView implements ProjectView {

	private final Composite composite;
	private final TreeViewer treeViewer;

	private final List<ProjectViewListener> listeners;
	
	private final ProjectsModel projectModel;
	
	SWTProjectView(SWTViewList viewList, TabFolder tabFolder, ProjectsModel projectModel) {

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		
		tabItem.setText("Projects");

		this.composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		this.composite.setLayout(new FillLayout());
		
		this.listeners = new ArrayList<>();
		
		this.treeViewer = new TreeViewer(this.composite);
		
		this.projectModel = projectModel;

		projectModel.addListener(new ProjectModelListener() {
			
			@Override
			public void onModelChanged() {
				
				System.out.println("### model changed");
				
				treeViewer.refresh();
			}
		});
		
		treeViewer.addDoubleClickListener(event -> {
			
			final ITreeSelection selection = (ITreeSelection)event.getSelection();
			
			final Object element = selection.getFirstElement();
			
			if (element instanceof SourceFileResourcePath) {
				for (ProjectViewListener listener : listeners) {
					listener.onSourceFileSelected((SourceFileResourcePath)element);
				}
			}
			else {
				treeViewer.setExpandedState(element, !treeViewer.getExpandedState(element));
			}
		});
		
		treeViewer.setContentProvider(new ProjectViewContentProvider(projectModel));

		treeViewer.setLabelProvider(new ProjectViewLabelProvider());
		
		treeViewer.setInput(projectModel);
		
		treeViewer.setSorter(new TreePathViewerSorter());
		
		treeViewer.setExpandedElements(new Object [] { projectModel.getRoot() });

		viewList.addView(this, composite);
	}
	
	Composite getComposite() {
		return composite;
	}

	@Override
	public Collection<ActionContext> getActiveActionContexts() {
		
		final ITreeSelection treeSelection = (ITreeSelection)treeViewer.getSelection();

		final List<ActionContext> contexts;
		
		if (treeSelection.isEmpty()) {
			contexts = null;
		}
		else {
			contexts = new ArrayList<>();

			
		}
		
		return contexts;
	}

	@Override
	public void addActionContextListener(ActionContextListener listener) {

		Objects.requireNonNull(listener);
		
		treeViewer.addSelectionChangedListener(event -> listener.onUpdated(getActiveActionContexts()));
	}

	@Override
	public void refresh() {
		treeViewer.refresh();
	}

	@Override
	public void addListener(ProjectViewListener listener) {
		
		Objects.requireNonNull(listener);
		
		if (listeners.contains(listener)) {
			throw new IllegalStateException();
		}
		
		listeners.add(listener);
	}

	@Override
	public void addKeyEventListener(KeyEventListener keyEventListener) {

		Objects.requireNonNull(keyEventListener);

		final SWTKeyEventListener swtKeyEventListener = new SWTKeyEventListener(keyEventListener);

		treeViewer.getControl().addKeyListener(swtKeyEventListener);
	}
	

	@Override
	public void showSourceFile(SourceFileResourcePath sourceFile, boolean setFocus) {

		Objects.requireNonNull(sourceFile);
		
		findElement(sourceFile, projectModel.getRoot(), projectModel);
		
		treeViewer.setSelection(new StructuredSelection(sourceFile), true);
		
		treeViewer.setExpandedState(sourceFile, true);
		
		if (setFocus) {
			treeViewer.getTree().setFocus();
		}
	}

	@Override
	public ResourcePath getSelected() {
		
		final ITreeSelection selection = (ITreeSelection)treeViewer.getSelection();
		
		return (ResourcePath)selection.getFirstElement();
	}

	private TreePath getTreePath(Object element) {

		final LinkedList<Object> elements = new LinkedList<>();
		
		final ITreeContentProvider treeContentProvider = (ITreeContentProvider)treeViewer.getContentProvider();
		
		elements.add(element);
		
		for (Object parent = treeContentProvider.getParent(element);
				parent != null;
				parent = treeContentProvider.getParent(parent)) {
			
			elements.addFirst(parent);
		}

		System.out.println("## set tree path " + elements.size() + "/" + elements);
		
		return new TreePath(elements.toArray(new Object[elements.size()]));
	}

	private static void findElement(ResourcePath element, ResourcePath cur, ProjectsModel projectModel) {
		
		//System.out.println("## compare " + element + " to " + cur);

		if (element.equals(cur) && element.hashCode() == cur.hashCode()) {

			System.out.println("Element found: " + element);
		}

		for (ResourcePath path : projectModel.getResources(cur)) {
			findElement(element, path, projectModel);
		}
	}
}
