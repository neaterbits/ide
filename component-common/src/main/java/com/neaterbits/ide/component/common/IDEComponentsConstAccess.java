package com.neaterbits.ide.component.common;

import java.util.List;

import com.neaterbits.ide.component.common.language.Languages;

public interface IDEComponentsConstAccess {

	Languages getLanguages();
	
	List<NewableCategory> getNewableCategories();
	
	UIComponentProvider findUIComponentProvider(NewableCategoryName category, Newable newable);
}
