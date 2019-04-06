package com.neaterbits.structuredlog.swt;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.structuredlog.binary.model.LogModel;

final class SWTBinaryLogUI extends SWTBaseUI {

	private final Composite composite;
	
	SWTBinaryLogUI(LogModel logModel) {

		Objects.requireNonNull(logModel);
		
		window.setSize(1500, 850);
		
		window.setLayout(new FillLayout());
		
		this.composite = new Composite(window, SWT.NONE);
		
		// composite.setLayout(new GridLayout(4, false));
		composite.setLayout(new GridLayout(2, false));
		
		final SWTBinaryLogTree logTree = new SWTBinaryLogTree(composite, SWT.NONE);
		
		logTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		logTree.setLogModel(logModel);

		final SWTBinaryLogOptions optionsComposite = new SWTBinaryLogOptions(composite, SWT.NONE, logModel);
		
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		optionsComposite.addUpdatedDisplayTypesListener(logTree::updateDisplayedTypes);
		
		final SWTBinaryLogMessageList messageList = new SWTBinaryLogMessageList(composite, SWT.NONE, logModel, logTree);
		
		logTree.addTreeSelectionListener(e -> messageList.refreshMessageList());

		optionsComposite.addShownListener(items -> {
			logTree.showItemsMatching(items);
			messageList.refreshMessageList();
		});
		
		optionsComposite.addHiddenListener(items -> {
			logTree.hideItemsMatching(items);
			messageList.refreshMessageList();
		});
		
		final GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		listGridData.horizontalSpan = 2;
		listGridData.heightHint = 250;

		messageList.setLayoutData(listGridData);

		window.open();
	}
}
