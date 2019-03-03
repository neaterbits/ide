package com.neaterbits.structuredlog.swt;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogData;
import com.neaterbits.structuredlog.model.LogDataEntry;
import com.neaterbits.structuredlog.model.LogEntry;

public final class LogUI {
	
	private final Shell window;
	private final Composite composite;

	private final List dataTypeList;
	private final Text dataListSearchText;
	private final List dataList;

	private String selectedDataType;
	
	LogUI(Log log) {

		Objects.requireNonNull(log);
		
		this.window = new Shell();
		
		window.setSize(1500, 850);
		
		window.setLayout(new FillLayout());
		
		this.composite = new Composite(window, SWT.NONE);
		
		// composite.setLayout(new GridLayout(4, false));
		composite.setLayout(new GridLayout(2, false));

		// window.addDisposeListener(event -> window.close());
		
		final Table table = createLogTable(composite, log);

		final GridData tableGridData = new GridData();
		
		tableGridData.grabExcessHorizontalSpace = true;
		tableGridData.horizontalSpan = 1;
		tableGridData.verticalSpan = 1;
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.horizontalAlignment = SWT.FILL;
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.verticalAlignment = SWT.FILL;
		
		table.setLayoutData(tableGridData);

		final Composite listsComposite = new Composite(composite, SWT.NONE);
		
		final GridData listsGridData = new GridData();
		
		listsGridData.grabExcessHorizontalSpace = false;
		listsGridData.horizontalSpan = 1;
		listsGridData.verticalSpan = 1;
		listsGridData.horizontalAlignment = SWT.BEGINNING;
		listsGridData.grabExcessVerticalSpace = true;
		listsGridData.verticalAlignment = SWT.FILL;

		listsGridData.widthHint = 500;
		
		listsComposite.setLayoutData(listsGridData);
		
		final GridLayout listsLayout = new GridLayout(1, true);
		// final FillLayout listsLayout = new FillLayout(SWT.VERTICAL);
		
		listsLayout.marginTop = 0;
		listsLayout.marginBottom = 0;
		listsLayout.marginLeft = 0;
		listsLayout.marginRight = 0;

		listsLayout.marginWidth = 0;
		
		listsComposite.setLayout(listsLayout);
		
		this.dataTypeList = new List(listsComposite, SWT.BORDER);

		final GridData dataTypeListGridData = new GridData();
		
		dataTypeListGridData.grabExcessHorizontalSpace = true;
		dataTypeListGridData.verticalIndent = 0;
		dataTypeListGridData.horizontalSpan = 1;
		// dataTypeListGridData.verticalSpan = 1;
		dataTypeListGridData.horizontalAlignment = SWT.FILL;
		dataTypeListGridData.grabExcessVerticalSpace = false;
		dataTypeListGridData.verticalAlignment = SWT.BEGINNING;
		dataTypeListGridData.heightHint = 250;

		dataTypeList.setLayoutData(dataTypeListGridData);
		
		this.dataListSearchText = new Text(listsComposite, SWT.BORDER);
		
		final GridData dataListSearchGridData = new GridData();
		
		dataListSearchGridData.grabExcessHorizontalSpace = true;
		dataListSearchGridData.horizontalSpan = 1;
		// dataListSearchGridData.verticalSpan = 1;
		dataListSearchGridData.horizontalAlignment = SWT.FILL;
		dataListSearchGridData.grabExcessVerticalSpace = false;
		dataListSearchGridData.verticalAlignment = SWT.BEGINNING;
		dataListSearchGridData.heightHint = 20;
		
		dataListSearchText.setLayoutData(dataListSearchGridData);
		
		dataListSearchText.addModifyListener(event -> {

			updateDataList(log, getSelectedLogData(log, table));
		});

		final GridData dataListGridData = new GridData();
		
		dataListGridData.grabExcessHorizontalSpace = true;
		dataListGridData.horizontalSpan = 1;
		// dataListGridData.verticalSpan = 1;
		dataListGridData.horizontalAlignment = SWT.FILL;
		dataListGridData.grabExcessVerticalSpace = true;
		dataListGridData.verticalAlignment = SWT.FILL;
		// dataListGridData.heightHint = 500;
		
		this.dataList = new List(listsComposite, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);

		dataList.setLayoutData(dataListGridData);
		
		dataTypeList.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {

				final LogData logData = getSelectedLogData(log, table);
				
				selectedDataType = logData.getType();
				
				updateDataList(log, logData);
			}
		});
		
		table.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				final TableItem tableItem = (TableItem)event.item;
				final LogEntry logEntry = log.getEntries().get(table.indexOf(tableItem));
				
				updateDataTypeList(log, logEntry);
			}
		});
		
		if (log.getEntries() != null && log.getEntries().size() > 0) {
			table.setSelection(0);
			updateDataTypeList(log, log.getEntries().get(0));
		}
		
		table.setFocus();
		
		window.open();
	}
	
	private LogData getSelectedLogData(Log log, Table table) {
		
		final LogEntry logEntry = log.getEntries().get(table.getSelectionIndex());
		
		final int dataTypeSelectionIdx = dataTypeList.getSelectionIndex();
		final LogData logData = logEntry.getData().get(dataTypeSelectionIdx);
		
		return logData;
	}
	
	private static String makePath(Log log, LogEntry logEntry) {
		
		return makePath(log, logEntry.getPathIndex());
	}

	private static String makePath(Log log, Integer pathIndex) {
		return makePath(log.getPaths().get(pathIndex).getEntries());
	}
		
	private static String makePath(java.util.List<String> list) {
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < list.size(); ++ i) {
		
			if (i > 0) {
				sb.append('#');
			}
			
			sb.append(list.get(i));
		}
		
		return sb.toString();
	}

	private void updateDataTypeList(Log log, LogEntry logEntry) {

		dataTypeList.removeAll();
		
		if (logEntry.getData() != null && !logEntry.getData().isEmpty()) {
			for (LogData data : logEntry.getData()) {
				dataTypeList.add(data.getType());
			}

			int selectionIndex = -1;
			
			if (selectedDataType != null) {

				for (int i = 0; i < logEntry.getData().size(); ++ i) {
					if (logEntry.getData().get(i).getType().equals(selectedDataType)) {
						selectionIndex = i;
						break;
					}
				}
			}
			
			if (selectionIndex == -1 && !logEntry.getData().isEmpty()) {
				selectionIndex = 0;
			}

			if (selectionIndex != -1) {
				dataTypeList.select(selectionIndex);

				updateDataList(log, logEntry.getData().get(selectionIndex));
			}
		}
	}
	
	private void updateDataList(Log log, LogData logData) {

		dataList.removeAll();

		final String searchText = dataListSearchText.getText().trim();
		
		if (logData.getEntries() != null) {
			for (LogDataEntry entry : logData.getEntries()) {
				
				final String text = entry.getPathIndex() != null ? makePath(log, entry.getPathIndex()) : entry.getData();
				
				if (searchText.isEmpty() || text.contains(searchText)) {
					dataList.add(text);
				}
			}
		}
		
		dataList.redraw();
	}
	
	private Table createLogTable(Composite composite, Log log) {
		
		final Table table = new Table(composite, SWT.BORDER|SWT.VIRTUAL);
		
		// final List list = new List(composite, SWT.NONE);
		
		table.setItemCount(log.getEntries().size());

		final TableColumn pathColumn = new TableColumn(table, SWT.BEGINNING);
		pathColumn.setText("Path");
		pathColumn.setWidth(650);
		
		final TableColumn messageColumn = new TableColumn(table, SWT.BEGINNING);
		messageColumn.setText("Message");
		messageColumn.setWidth(350);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		table.addListener(SWT.SetData, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				final TableItem item = (TableItem)event.item;
				
				final LogEntry logEntry = log.getEntries().get(table.indexOf(item));
				
				item.setText(new String [] {
						logEntry.getPathIndex() != null ? makePath(log, logEntry) : "",
						logEntry.getLogMessage() });
			}
		});
		
		return table;
	}

	void start() {

		final Display display = window.getDisplay();
		
		while (display.readAndDispatch() && !window.isDisposed()) {
			display.sleep();
		}
	}
}
