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

		final SWTBinaryLogOptions optionsComposite = new SWTBinaryLogOptions(composite, SWT.NONE, logModel, logTree);
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		final SWTBinaryLogMessageList messageList = new SWTBinaryLogMessageList(composite, SWT.NONE, logModel, logTree);
		
		final GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		listGridData.horizontalSpan = 2;
		listGridData.heightHint = 250;

		messageList.setLayoutData(listGridData);

		window.open();
	}
}
