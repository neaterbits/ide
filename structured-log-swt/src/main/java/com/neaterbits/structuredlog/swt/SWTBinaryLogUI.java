package com.neaterbits.structuredlog.swt;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.binary.model.LogObject;

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
		
		logTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		
		logTree.setLogModel(logModel);
		
		final List<String> distinctSimpleTypes = logModel.getLogObjects().stream()
				.map(LogObject::getSimpleType)
				.distinct()
				.collect(Collectors.toList());
		
		Collections.sort(distinctSimpleTypes);
		
		final Composite optionsComposite = new Composite(composite, SWT.NONE);
		
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		optionsComposite.setLayout(new RowLayout(SWT.VERTICAL));
		
		final Set<String> displayedTypes = new HashSet<>(distinctSimpleTypes);
		
		for (String simpleType : distinctSimpleTypes) {
			
			final Button checkButton = new Button(optionsComposite, SWT.CHECK);
			
			checkButton.setText(simpleType);
			
			checkButton.setSelection(true);
			
			checkButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (checkButton.getSelection()) {
						displayedTypes.add(simpleType);
					}
					else {
						displayedTypes.remove(simpleType);
					}
					
					logTree.updateDisplayedTypes(displayedTypes);
				}
			});
		}
		
		window.open();
	}
}
