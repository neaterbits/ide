package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class SWTBinaryLogTreeFilter {

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
				
					if (onRangeMatch != null) {
						onRangeMatch.accept(idx, filter.length());
					}
					
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
	
	
	static void filter(
			Object rootElement,
			String [] showItems,
			String [] hideItems,
			FilteredTexts filteredTexts,
			Set<Object> hiddenElements,
			IStructuredContentProvider contentProvider,
			Function<Object, String> labelProvider) {

	
		filter(rootElement, showItems, hideItems, filteredTexts, hiddenElements, contentProvider, labelProvider, false);
		
	}

	static boolean filter(
			Object element,
			String [] showItems,
			String [] hideItems,
			FilteredTexts filteredTexts,
			Set<Object> hiddenElements,
			IStructuredContentProvider contentProvider,
			Function<Object, String> labelProvider,
			boolean ancestorShown) {

		final boolean visible;

		final String label = labelProvider.apply(element);

		final boolean hidden = hideItems != null && hideItems.length != 0
				? stringMatchesFilters(label, hideItems, null)
				: false;
		
		if (hidden) {
			addToHidden(element, hiddenElements, contentProvider);
			
			visible = false;
		}
		else if (showItems == null || showItems.length == 0) {
			// Always show subitems, look for hide elements
			
			visible = filterSubElements(
					element,
					showItems, hideItems,
					filteredTexts, hiddenElements,
					contentProvider, labelProvider,
					ancestorShown);
		}
		else {
			
			if (ancestorShown) {
				// Show whole subtree below filter shown ancestor but add any matching texts
				visible = true;

				matchAndAddStyles(label, showItems, element, filteredTexts);
			}
			else {
				
				// This node shown ?
				
				final boolean match = matchAndAddStyles(label, showItems, element, filteredTexts);
				
				if (match) {
					// Show this element and ancestor elements
					visible = true;

					final Object [] subElements = contentProvider.getElements(element);
					
					if (subElements != null) {
						
						for (Object subElement : subElements) {

							filter(
									subElement,
									showItems, hideItems,
									filteredTexts, hiddenElements,
									contentProvider, labelProvider,
									true);
						}
					}
				}
				else {
					// hide this branch unless a subelement is shown
					
					final boolean subVisible = filterSubElements(
							element,
							showItems, hideItems,
							filteredTexts, hiddenElements,
							contentProvider, labelProvider,
							false);
					
					if (!subVisible) {
						hiddenElements.add(element);
					}
					
					visible = subVisible;
				}
			}
		}
		
		return visible;
	}
	
	private static boolean filterSubElements(
			Object element,
			String [] showItems,
			String [] hideItems,
			FilteredTexts filteredTexts,
			Set<Object> hiddenElements,
			IStructuredContentProvider contentProvider,
			Function<Object, String> labelProvider,
			boolean ancestorShown) {
		
		boolean subVisible;
		
		final Object [] subElements = contentProvider.getElements(element);
		
		if (subElements == null || subElements.length == 0) {
			subVisible = false;
		}
		else {
			
			subVisible = false;
			
			for (Object subElement : subElements) {
				
				final boolean sub = filter(
						subElement,
						showItems, hideItems,
						filteredTexts, hiddenElements,
						contentProvider, labelProvider,
						false);
				
				if (sub) {
					subVisible = true;
				}
			}
		}

		return subVisible;
	}

	private static boolean matchAndAddStyles(String label, String [] showItems, Object element, FilteredTexts filteredTexts) {

		final List<TextStyleOffset> offsets = new ArrayList<>();

		final boolean match = stringMatchesFilters(label, showItems, (offset, length) -> {
			
			// System.out.println("## add text style from " + offset + "/" + length);
			
			offsets.add(new TextStyleOffset(offset, length, null, TextStyleOffset.SELECTED_BG_COLOR));
		});

		if (match) {
			offsets.forEach(offset -> filteredTexts.addStyleOffset(element, offset));
		}
		
		return match;
	}
	
	private static void addToHidden(
			Object element,
			Set<Object> hiddenElements,
			IStructuredContentProvider contentProvider) {
		
		Objects.requireNonNull(element);
		
		hiddenElements.add(element);
		
		final Object [] subElements = contentProvider.getElements(element);
		
		if (subElements != null) {
			for (Object subElement : subElements) {
				addToHidden(subElement, hiddenElements, contentProvider);
			}
		}
	}
}

