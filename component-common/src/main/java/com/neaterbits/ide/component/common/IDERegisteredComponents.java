package com.neaterbits.ide.component.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.component.common.language.LanguageComponent;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.language.Languages;

public final class IDERegisteredComponents implements IDEComponentsConstAccess {

	private final List<IDERegisteredComponent> components;

	private final Map<LanguageName, LanguageComponent> languageComponents;
	
	private final Languages languages;
	
	public IDERegisteredComponents() {
		this.components = new ArrayList<>();
	
		this.languageComponents = new HashMap<>();
		
		this.languages = new Languages() {
			@Override
			public LanguageComponent getLanguageComponent(LanguageName languageName) {
				Objects.requireNonNull(languageName);

				return languageComponents.get(languageName);
			}

			@Override
			public LanguageName detectLanguage(SourceFileResourcePath sourceFile) {
				
				final String fileName = sourceFile.getName();
				
				final int suffixIndex = fileName.lastIndexOf('.');
				
				if (suffixIndex >= 0 && suffixIndex < fileName.length() - 1) {
				
					final String suffix = fileName.substring(suffixIndex + 1);
					
					for (LanguageComponent language : languageComponents.values()) {
						for (String languageSuffix : language.getFileSuffixes()) {
							if (suffix.toLowerCase().equals(languageSuffix.toLowerCase())) {
								return language.getLanguageName();
							}
						}
					}
				}
				
				return null;
			}
		};
	}

	@Override
	public Languages getLanguages() {
		return languages;
	}
	
	public void registerComponent(ComponentProvider componentProvider, UIComponentProvider uiComponentProvider) {

		if (componentProvider instanceof LanguageComponent) {
			final LanguageComponent languageComponent = (LanguageComponent)componentProvider;
		
			languageComponents.put(languageComponent.getLanguageName(), languageComponent);
		}
		
		components.add(new IDERegisteredComponent(componentProvider, uiComponentProvider));
	}
	
	@Override
	public List<NewableCategory> getNewableCategories() {
		
		final Map<NewableCategory, Set<Newable>> map = new HashMap<>();
		
		for (IDERegisteredComponent ideComponent : components) {
			
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
		
		return Collections.unmodifiableList(categories);
	}
	
	@Override
	public UIComponentProvider findUIComponentProvider(NewableCategoryName category, Newable newable) {
		
		Objects.requireNonNull(category);
		Objects.requireNonNull(newable);
		
		for (IDERegisteredComponent ideComponent : components) {
			
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
