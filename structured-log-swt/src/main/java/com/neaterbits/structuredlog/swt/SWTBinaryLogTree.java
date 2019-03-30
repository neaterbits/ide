package com.neaterbits.structuredlog.swt;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.binary.model.LogObject;

public final class SWTBinaryLogTree extends Composite {

	private final TreeViewer treeViewer;
	private final SWTBinaryLogTreeContentProvider contentProvider;
	private final IBaseLabelProvider labelProvider;

	public SWTBinaryLogTree(Composite parent, int style) {
		super(parent, style);
	
		setLayout(new FillLayout());
		
		this.treeViewer = new TreeViewer(this);
		
		// treeViewer.setUseHashlookup(true);
	
		final FilteredTexts filteredTexts = new FilteredTexts();
		
		this.contentProvider = new SWTBinaryLogTreeContentProvider(filteredTexts);
		treeViewer.setContentProvider(contentProvider);	

		this.labelProvider = new DelegatingStyledCellLabelProvider(new SWTBinaryLogTreeLabelProvider(filteredTexts));
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
	
	void select(LogObject logObject) {
		
		Objects.requireNonNull(logObject);
		
		treeViewer.setSelection(new StructuredSelection(logObject));
	}
	
	void showItemsMatching(String [] filters) {
		contentProvider.showItemsMatching(filters);

		treeViewer.refresh();
		
		treeViewer.expandAll();
	}
	
	void hideItemsMatching(String [] filters) {
		contentProvider.hideItemsMatching(filters);
		
		treeViewer.refresh();
	}
	
	static String getLogObjectLabel(LogObject logObject) {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append(logObject.getSimpleType());
		final String identifierText;
		
		if (logObject.getLogLocalIdentifier() != null) {
			identifierText = logObject.getLogLocalIdentifier();
		}
		else if (logObject.getLogIdentifier() != null) {
			identifierText = logObject.getLogIdentifier();
		}
		else {
			identifierText = null;
		}
		
		if (identifierText != null) {
			sb.append(' ');
			sb.append(identifierText);
		}
		
		if (logObject.getDescription() != null) {
			sb.append(' ');
			sb.append(logObject.getDescription());
		}
		
		return sb.toString();
	}
}
