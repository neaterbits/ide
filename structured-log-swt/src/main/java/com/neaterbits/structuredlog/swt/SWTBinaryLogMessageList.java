package com.neaterbits.structuredlog.swt;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.structuredlog.binary.model.LogMessage;
import com.neaterbits.structuredlog.binary.model.LogModel;

final class SWTBinaryLogMessageList extends Composite {

	private final ListViewer logMessageViewer;
	
	public SWTBinaryLogMessageList(Composite parent, int style, LogModel logModel, SWTBinaryLogTree logTree) {
		super(parent, style);

		setLayout(new FillLayout());
		
		final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(this, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);

		this.logMessageViewer = new ListViewer(list);
		
		logMessageViewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {

				final LogModel logModel = (LogModel)inputElement;

				final List<LogMessage> logMessages = logModel.getMessages();
				
				return logMessages.toArray(new Object[logMessages.size()]);
			}
		});
		
		logMessageViewer.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
			
				final LogMessage logMessage = (LogMessage)element;
				
				return logMessage.getMessage();
			}
		});
		
		logMessageViewer.setFilters(new ViewerFilter [] {
				new ViewerFilter() {
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {

						final LogMessage logMessage = (LogMessage)element;
						
						return logTree.shouldShowMessageInLog(logMessage.getTarget());
					}
				}
		});

		logMessageViewer.addDoubleClickListener(event -> {
			final IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			
			final LogMessage logMessage = (LogMessage)selection.getFirstElement();
			
			if (logMessage != null && logMessage.getTarget() != null) {
				logTree.select(logMessage.getTarget());
			}
		});
		
		
		logMessageViewer.setInput(logModel);
	}

	void refreshMessageList() {
		logMessageViewer.refresh();
	}
}
