package com.neaterbits.ide.swt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.build.model.compile.BuildIssue;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.actions.contexts.BuildIssueContext;
import com.neaterbits.ide.common.ui.view.ActionContextListener;
import com.neaterbits.ide.common.ui.view.BuildIssuesView;

public class SWTBuildIssuesView extends SWTView implements BuildIssuesView {

	private final TableViewer tableViewer;
	
	public SWTBuildIssuesView(TabFolder tabFolder) {
		
		this.tableViewer = new TableViewer(tabFolder);

		setIDEView(this, tableViewer.getTable());

		// final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		
		// tabItem.setText("Build issues");

		tableViewer.setContentProvider(new ContentProvider());
		
		//tabItem.setControl(tableViewer.getControl());
		
	}

	@Override
	public Collection<ActionContext> getActiveActionContexts() {

		final IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		
		return selection.isEmpty()
				? null
				: Arrays.asList(new BuildIssueContext((BuildIssue)selection.getFirstElement()));
	}
	
	@Override
	public void addActionContextListener(ActionContextListener listener) {
		tableViewer.addSelectionChangedListener(event -> listener.onUpdated(getActiveActionContexts()));
	}

	@Override
	public void update(List<BuildIssue> problems) {
		tableViewer.setInput(problems);
	}
	
	private static class ContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			
			final BuildIssue problem = (BuildIssue)inputElement;
			
			return new Object [] {
					problem.getDescription(),
					problem.getResource().getFile().getName(),
					problem.getType()
			};
		}
	}
}
