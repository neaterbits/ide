package com.neaterbits.structuredlog.swt;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.structuredlog.binary.model.LogModel;

public final class SWTBinaryLogTree extends Composite {

	private final TreeViewer treeViewer;
	private final SWTBinaryLogTreeContentProvider contentProvider;
	private final SWTBinaryLogTreeLabelProvider labelProvider;

	public SWTBinaryLogTree(Composite parent, int style) {
		super(parent, style);
	
		setLayout(new FillLayout());
		
		this.treeViewer = new TreeViewer(this);
		
		treeViewer.setUseHashlookup(true);
	
		this.contentProvider = new SWTBinaryLogTreeContentProvider();
		treeViewer.setContentProvider(contentProvider);	

		this.labelProvider = new SWTBinaryLogTreeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
	}
	
	void setLogModel(LogModel logModel) {
		
		Objects.requireNonNull(logModel);
		
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		
		treeViewer.setInput(logModel);

		//treeViewer.setExpandedElements(new Object [] { logModel });
	}

	void updateDisplayedTypes(Set<String> displayedTypes) {
		contentProvider.updateDisplayedTypes(displayedTypes);
		
		treeViewer.refresh();
		
		treeViewer.expandAll();
	}
}
