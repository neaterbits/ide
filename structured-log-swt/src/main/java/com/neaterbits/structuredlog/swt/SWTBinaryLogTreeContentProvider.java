package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.jface.viewers.Viewer;

import com.neaterbits.ide.util.swt.TreeContentAdapter;
import com.neaterbits.ide.util.ui.text.styling.TextColor;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.structuredlog.binary.model.LogCollectionField;
import com.neaterbits.structuredlog.binary.model.LogField;
import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.binary.model.LogNode;
import com.neaterbits.structuredlog.binary.model.LogObject;
import com.neaterbits.structuredlog.binary.model.LoggableField;

final class SWTBinaryLogTreeContentProvider extends TreeContentAdapter {

	private final FilteredTexts filteredTexts;
	
	private Set<String> displayedTypes;
	
	private String [] showItems;
	private String [] hideItems;
	
	
	SWTBinaryLogTreeContentProvider(FilteredTexts filteredTexts) {

		Objects.requireNonNull(filteredTexts);
		
		this.filteredTexts = filteredTexts;
	}


	void updateDisplayedTypes(Set<String> displayedTypes) {
		this.displayedTypes = new HashSet<>(displayedTypes);
	}
	
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
	
	@Override
	public Object getParent(Object element) {
		
		final Object obj;
		
		if (element instanceof LogModel) {
			obj = null;
		}
		else {
			final LogNode node = (LogNode)element;
		
			obj = node.getParent();
		}
		
		return obj;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		
		System.out.println("## get elements for " + inputElement);
		
		final Object [] objs;
		
		if (inputElement instanceof LogModel) {
	
			final LogModel model = (LogModel)inputElement;
			
			objs = model.getLogRootObjects().toArray(new Object[model.getLogRootObjects().size()]);
		}
		else if (inputElement instanceof LogObject) {
			
			final LogObject logObject = (LogObject)inputElement;
			
			final Collection<LogField> fields = logObject.getFields();
			
			if (fields == null || fields.isEmpty()) {
				objs = null;
			}
			else if (fields.size() == 1) {
				objs = getElements(fields.iterator().next());
			}
			else {
				objs = fields.toArray(new Object[fields.size()]);
			}
		}
		else if (inputElement instanceof LogCollectionField) {
			final Collection<LogObject> objects = ((LogCollectionField)inputElement).getCollection();
		
			if (objects != null) {
				
				final List<Object> list = new ArrayList<>(objects.size());
				
				final Iterator<LogObject> iter = objects.iterator();

				while (iter.hasNext()) {
					
					final LogObject obj = iter.next();
					
					if (!shouldDisplayType(obj)) {
						
						// Skip and get sub type
						final Object [] sub = getElements(obj);
						
						for (int j = 0; j < sub.length; ++ j) {
							list.add(sub[j]);
						}
					}
					else {
						
						final Object toAdd = checkElementVisible(obj);
						
						if (toAdd != null) {
							list.add(obj);
						}
					}
				}

				objs = list.toArray(new Object[list.size()]);
			}
			
			else {
				objs = null;
			}
		}
		else if (inputElement instanceof LoggableField) {
			
			final LogObject logObject = ((LoggableField)inputElement).getObject();

			final Object toAdd = checkElementVisible(logObject);
			
			objs = toAdd != null ? new Object [] { toAdd } : null;
		}
		else {
			objs = null;
		}

		System.out.println("## return " + (objs != null ? Arrays.toString(objs) : null));
		
		return objs;
	}
	
	private Object checkElementVisible(Object element) {
		
		final List<TextStyleOffset> offsets = new ArrayList<>();
		
		final boolean visible = isElementVisible(element, (offset, length) -> {
			
			if (element instanceof LogObject) {
				offsets.add(new TextStyleOffset(offset, length, null, TextStyleOffset.SELECTED_BG_COLOR));
			}
		});
	
		if (element instanceof LogObject) {
			final LogObject logObject = (LogObject)element;

			if (visible) {
				offsets.forEach(offset -> filteredTexts.addStyleOffset(logObject, offset));
			}
			else {
				final String label = SWTBinaryLogTree.getLogObjectLabel(logObject);
				
				filteredTexts.addStyleOffset(
						(LogObject)element,
						new TextStyleOffset(0, label.length(), new TextColor(0xA0, 0xA0, 0xA0)));
			}
		}
		
		return element;
	}
	
	private Object [] checkElementVisibleWithSub(Object element) {
		return null;
	}
	

	private boolean shouldDisplayType(LogObject logObject) {
		
		return displayedTypes == null || displayedTypes.contains(logObject.getSimpleType());
		
	}

	
	
	private boolean isElementVisible(Object element, BiConsumer<Integer, Integer> onRangeMatch) {

		final boolean visible;
		
		if (hasSelfOrAncestorFilter(element, hideItems, onRangeMatch)) {
			visible = false;
		}
		else if (hasSelfOrAncestorFilter(element, showItems, onRangeMatch)) {
			visible = true;
		}
		else if (showItems != null && showItems.length > 0) {
			// show item filters but did not match
			visible = false;
		}
		else {
			visible = true;
		}
		
		return visible;
	}
	
	private boolean hasSelfOrAncestorFilter(Object element, String [] filters, BiConsumer<Integer, Integer> onRangeMatch) {
		
		for (; element != null; element = getParent(element)) {
			if (matchesFilters(element, filters, onRangeMatch)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private static boolean matchesFilters(Object element, String [] filters, BiConsumer<Integer, Integer> onRangeMatch) {
		
		final boolean matches;
		
		Objects.requireNonNull(element);
		
		if (filters == null || filters.length == 0) {
			matches = false;
		}
		else {

			if (element instanceof LogObject) {
				
				final LogObject logObject = (LogObject)element;
				
				matches = logObject.getDescription() != null
						? stringMatchesFilters(logObject.getDescription(), filters, onRangeMatch)
						: true;
			}
			else if (element instanceof LogField) {
				matches = true;
			}
			else {
				throw new UnsupportedOperationException();
			}
		}

		return matches;
	}
	
	private static boolean stringMatchesFilters(String string, String [] filters, BiConsumer<Integer, Integer> onRangeMatch) {

		Objects.requireNonNull(string);
		Objects.requireNonNull(filters);
		
		final String lowercaseString = string.toLowerCase();
		
		int curIdx = 0;

		boolean matchFound = false;
		
		for (;;) {
			
			boolean filterMatchFound = false;
			
			for (String filter : filters) {
				
				final int idx = lowercaseString.indexOf(filter.toLowerCase(), curIdx);

				if (idx >= 0) {
					
					onRangeMatch.accept(curIdx, filter.length());
					
					curIdx = idx + filter.length();
					
					matchFound = true;
					
					filterMatchFound = true;
				}
			}
			
			if (!filterMatchFound) {
				break;
			}
		}
		

		return matchFound;
	}
	
	
	void showItemsMatching(String [] filters) {
		this.showItems = Arrays.copyOf(filters, filters.length);
		
	}
	
	void hideItemsMatching(String [] filters) {
		this.hideItems = Arrays.copyOf(filters, filters.length);
	}
}

