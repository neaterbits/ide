package com.neaterbits.ide.component.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class IDEComponents<WINDOW> {

	private final List<IDEComponent<WINDOW>> components;

	public IDEComponents() {
		this.components = new ArrayList<>();
	}

	public void registerComponent(ComponentProvider componentProvider, UIComponentProvider<WINDOW> uiComponentProvider) {

		components.add(new IDEComponent<>(componentProvider, uiComponentProvider));
		
	}
	
	public List<NewableCategory> getNewableCategories() {
		
		final Map<NewableCategory, Set<Newable>> map = new HashMap<>();
		
		for (IDEComponent<WINDOW> ideComponent : components) {
			
			final List<NewableCategory> componentNewableCategories = ideComponent.getComponentProvider().getNewables();
			
			if (componentNewableCategories != null) {
				for (NewableCategory newableCategory : componentNewableCategories) {
	
					if (newableCategory.getTypes() == null || newableCategory.getTypes().isEmpty()) {
						throw new IllegalStateException();
					}
					
					Set<Newable> newables = map.get(newableCategory);
					
					if (newables == null) {
						newables = new HashSet<>();
						
						map.put(newableCategory, newables);
					}
					
					for (Newable newable : newableCategory.getTypes()) {
						newables.add(newable);
					}
				}
			}
		}
		
		final List<NewableCategory> categories = new ArrayList<>(map.size());

		for (Map.Entry<NewableCategory, Set<Newable>> entry : map.entrySet()) {
			
			final NewableCategory category = new NewableCategory(
					entry.getKey().getName(),
					entry.getKey().getDisplayName(),
					new ArrayList<>(entry.getValue()));
			
			Collections.sort(
					category.getTypes(),
					(newable1, newable2) -> newable1.getDisplayName().compareTo(newable2.getDisplayName()));
			
			categories.add(category);
		}
		
		Collections.sort(
				categories,
				(category1, category2) -> category1.getDisplayName().compareTo(category2.getDisplayName()));
		
		return categories;
	}
	
	public UIComponentProvider<WINDOW> findUIComponentProvider(NewableCategoryName category, Newable newable) {
		
		Objects.requireNonNull(category);
		Objects.requireNonNull(newable);
		
		for (IDEComponent<WINDOW> ideComponent : components) {
			
			if (ideComponent.getComponentProvider().getNewables() != null) {
				for (NewableCategory componentCategory : ideComponent.getComponentProvider().getNewables()) {
					if (category.getName().equals(componentCategory.getName())) {
						for (Newable componentNewable : componentCategory.getTypes()) {
							if (newable.equals(componentNewable)) {
								return ideComponent.getUiComponentProvider();
							}
						}
					}
				}
			}
		}

		return null;
	}
}
