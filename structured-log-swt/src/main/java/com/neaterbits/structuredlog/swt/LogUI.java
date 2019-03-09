package com.neaterbits.structuredlog.swt;

import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
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
	private Table table;

	private final List dataTypeList;
	private final Text dataListSearchText;
	private final List dataList;

	private String selectedDataType;
	
	private java.util.List<LogEntry> tableLogEntries;
	
	LogUI(Log log) {

		Objects.requireNonNull(log);
		
		this.window = new Shell();
		
		window.setSize(1850, 850);
		
		window.setLayout(new FillLayout());
		
		this.composite = new Composite(window, SWT.NONE);
		
		// composite.setLayout(new GridLayout(4, false));
		composite.setLayout(new GridLayout(2, false));

		// window.addDisposeListener(event -> window.close());

		final Composite tableComposite = new Composite(composite, SWT.NONE);
		
		tableComposite.setLayout(new GridLayout(1, true));
		
		final GridData tableCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		tableCompositeGridData.horizontalSpan = 1;
		tableCompositeGridData.verticalSpan = 1;
		
		tableComposite.setLayoutData(tableCompositeGridData);

		final Text pathSearchText = new Text(tableComposite, SWT.BORDER);
		final GridData pathSearchTextGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
		pathSearchTextGridData.widthHint = 200;
		pathSearchText.setLayoutData(pathSearchTextGridData);
		pathSearchText.addModifyListener(e -> {

			updateTableLogEntries(log, pathSearchText.getText());
			
			table.redraw();
		});
		
		final Label pathLabel = new Label(tableComposite, SWT.NONE);
		final GridData pathLabelGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		pathLabel.setLayoutData(pathLabelGridData);

		final Label messageLabel = new Label(tableComposite, SWT.WRAP);
		final GridData messageLabelGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		messageLabelGridData.heightHint = 75;
		messageLabel.setLayoutData(messageLabelGridData);
		
		final GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		this.table = createLogTable(tableComposite, log);

		table.setLayoutData(tableGridData);

		final Composite listsComposite = new Composite(composite, SWT.NONE);
		
		final GridData listsGridData = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
		
		listsGridData.horizontalSpan = 1;
		listsGridData.verticalSpan = 1;

		listsGridData.widthHint = 650;
		
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

		final GridData dataTypeListGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		
		dataTypeListGridData.verticalIndent = 0;
		dataTypeListGridData.horizontalSpan = 1;
		// dataTypeListGridData.verticalSpan = 1;
		dataTypeListGridData.heightHint = 250;

		dataTypeList.setLayoutData(dataTypeListGridData);
		
		this.dataListSearchText = new Text(listsComposite, SWT.BORDER);
		
		final GridData dataListSearchGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		
		dataListSearchGridData.horizontalSpan = 1;
		// dataListSearchGridData.verticalSpan = 1;
		dataListSearchGridData.heightHint = 20;
		
		dataListSearchText.setLayoutData(dataListSearchGridData);
		
		dataListSearchText.addModifyListener(event -> {
			updateDataList(log, getSelectedLogData(log, table));
		});

		final GridData dataListGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		dataListGridData.horizontalSpan = 1;
		// dataListGridData.verticalSpan = 1;
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
				final LogEntry logEntry = tableLogEntries.get(table.indexOf(tableItem));
				
				updateDataTypeList(log, logEntry);

				updateLabels(pathLabel, messageLabel, log, logEntry);
			}
		});
		
		if (log.getEntries() != null && log.getEntries().size() > 0) {
			table.setSelection(0);

			updateDataTypeList(log, log.getEntries().get(0));
			
			updateTableLogEntries(log, null);
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

	private void updateLabels(Label pathLabel, Label messageLabel, Log log, LogEntry logEntry) {

		final String pathText = logEntry.getPathIndex() != null ? makePath(log, logEntry) : null;
		
		pathLabel.setText("Path:" + (pathText != null ? " " + pathText : ""));
		
		messageLabel.setText("Message:" + (logEntry.getLogMessage() != null ? " " + logEntry.getLogMessage() : ""));
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
	
	private void updateTableLogEntries(Log log, String pathFilter) {
		
		if (pathFilter == null || pathFilter.trim().isEmpty()) {
			tableLogEntries = log.getEntries();
		}
		else {
			final String trimmed = pathFilter.trim();

			tableLogEntries = log.getEntries().stream()
					.filter(entry -> {
						
						final String path = entry.getPathIndex() != null ? makePath(log, entry) : null;
						
						return path != null ? path.contains(trimmed) : false;
					})
					.collect(Collectors.toList());

			System.out.println("## table log entries " + tableLogEntries.size() + " from " + log.getEntries().size());
		}

		table.setItemCount(0);
		table.setItemCount(tableLogEntries.size());
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
				
				final LogEntry logEntry = tableLogEntries.get(table.indexOf(item));
				
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
