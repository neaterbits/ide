package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.binary.model.LogObject;

final class SWTBinaryLogOptions extends Composite {

	private final List<Consumer<Set<String>>> updatedDisplayTypesListeners;
	private final List<Consumer<String []>> shownListeners;
	private final List<Consumer<String []>> hiddenListeners;
	
	SWTBinaryLogOptions(Composite parent, int style, LogModel logModel) {
		super(parent, style);

		setLayout(new GridLayout(1, false));

		this.updatedDisplayTypesListeners = new ArrayList<>();
		this.shownListeners = new ArrayList<>();
		this.hiddenListeners = new ArrayList<>();
		
		final Group typesGroup = makeTypesGroup(
				this,
				logModel,
				displayedTypes -> updatedDisplayTypesListeners.forEach(listener -> listener.accept(displayedTypes)));
		
		typesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		final Group filterGroup = makeFiltersGroup(
				this,
				list -> shownListeners.forEach(listener -> listener.accept(list)),
				list -> hiddenListeners.forEach(listener -> listener.accept(list)));
		
		filterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
	}
	
	void addUpdatedDisplayTypesListener(Consumer<Set<String>> listener) {
		
		Objects.requireNonNull(listener);
		
		updatedDisplayTypesListeners.add(listener);
	}
	
	void addShownListener(Consumer<String[]> listener) {
		
		Objects.requireNonNull(listener);
		
		shownListeners.add(listener);
	}
	
	void addHiddenListener(Consumer<String[]> listener) {
		
		Objects.requireNonNull(listener);
		
		hiddenListeners.add(listener);
	}

	private static Group makeTypesGroup(Composite composite, LogModel logModel, Consumer<Set<String>> onUpdatedDisplayedTypes) {
		
		final Group typesGroup = new Group(composite, SWT.BORDER);
		typesGroup.setText("Types");
		typesGroup.setLayout(new RowLayout(SWT.VERTICAL));

		final List<String> distinctSimpleTypes = logModel.getLogObjects().stream()
				.map(LogObject::getSimpleType)
				.distinct()
				.collect(Collectors.toList());
		
		Collections.sort(distinctSimpleTypes);

		final Set<String> displayedTypes = new HashSet<>(distinctSimpleTypes);
		
		for (String simpleType : distinctSimpleTypes) {
			
			final Button checkButton = new Button(typesGroup, SWT.CHECK);
			
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

					onUpdatedDisplayedTypes.accept(displayedTypes);
				}
			});
		}

		return typesGroup;
	}

	private static Group makeFiltersGroup(Composite composite, Consumer<String[]> onShownUpdated, Consumer<String[]> onHiddenUpdated) {

		final Group group = new Group(composite, SWT.BORDER);
		group.setText("Filters");

		group.setLayout(new GridLayout(2, false));
		
		final Composite showFilterComposite = makeFilterEdit(group, "Show", onShownUpdated);
		showFilterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		final Composite hideFilterComposite = makeFilterEdit(group, "Hide", onHiddenUpdated);
		hideFilterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		return group;
	}

	private static Composite makeFilterEdit(Composite composite, String title, Consumer<String[]> onFiltersUpdated) {

		final Composite filterComposite = new Composite(composite, SWT.NONE);
		filterComposite.setLayout(new GridLayout(2, false));
		
		final Label label = new Label(filterComposite, SWT.NONE);
		label.setText(title);
		
		final GridData labelGridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		labelGridData.horizontalSpan = 2;

		label.setLayoutData(labelGridData);
		
		final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(filterComposite, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL);
		
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Composite actionsComposite = new Composite(filterComposite, SWT.NONE);
		
		actionsComposite.setLayout(new GridLayout(1, false));
		actionsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
		
		final Button addButton = new Button(actionsComposite, SWT.PUSH);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		addButton.setText("Add");
		
		final Button removeButton = new Button(actionsComposite, SWT.PUSH);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		removeButton.setText("Remove");
		
		removeButton.setEnabled(false);

		final Text filterText = new Text(filterComposite, SWT.BORDER);
		final GridData filterTextGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		filterTextGridData.horizontalSpan = 2;
		
		filterText.setEnabled(false);
		
		filterText.setLayoutData(filterTextGridData);

		list.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				final int selectionCount = list.getSelectionCount();
				
				removeButton.setEnabled(selectionCount > 0);

				updateFilterText(list, filterText);
			}
		});
		
		filterText.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				if (list.getSelectionCount() > 0) {
					
					final String trimmed = filterText.getText().trim();
					
					final int selectionIndex = list.getSelectionIndex();
					
					if (trimmed.isEmpty()) {
						list.remove(selectionIndex);
					}
					else {
						list.setItem(selectionIndex, trimmed);
					}

					onFiltersUpdated.accept(list.getItems());
				}
			}
		});

		
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (list.getItemCount() == 0) {
					addEmptyListEntry(list, filterText);
				}
				else if (!list.getItem(list.getItemCount() - 1).trim().isEmpty()) {
					addEmptyListEntry(list, filterText);
				}
				else {
					list.setSelection(list.getItemCount() - 1);

					// Can be string with spaces, update filter text
					filterText.setText(list.getItem(list.getItemCount() - 1)); 
				}

				filterText.setEnabled(true);
				filterText.setFocus();
				removeButton.setEnabled(true);
			}
		});

		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				list.remove(list.getSelectionIndex());

				updateFilterText(list, filterText);
				
				removeButton.setEnabled(false);
				
				onFiltersUpdated.accept(list.getItems());
			}
		});

		return filterComposite;
	}
	
	private static void updateFilterText(org.eclipse.swt.widgets.List list, Text filterText) {
		
		if (list.getSelectionCount() > 0) {
			filterText.setText(list.getSelection()[0]);
			filterText.setEnabled(true);
			filterText.setFocus();
		}
		else {
			filterText.setEnabled(false);
			filterText.setText("");
		}
	}
	
	private static void addEmptyListEntry(org.eclipse.swt.widgets.List list, Text text) {
		list.add("");
		list.setSelection(list.getItemCount() - 1);
		text.setFocus();
	}
}
