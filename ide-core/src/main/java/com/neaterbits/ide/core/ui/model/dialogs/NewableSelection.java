package com.neaterbits.ide.core.ui.model.dialogs;

import java.util.Objects;

import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategoryName;

public final class NewableSelection {

	private final NewableCategoryName category;
	private final Newable newable;
	
	public NewableSelection(NewableCategoryName category, Newable newable) {
		
		Objects.requireNonNull(category);
		Objects.requireNonNull(newable);
		
		this.category = category;
		this.newable = newable;
	}

	public NewableCategoryName getCategory() {
		return category;
	}

	public Newable getNewable() {
		return newable;
	}

	@Override
	public String toString() {
		return "NewableSelection [category=" + category + ", newable=" + newable + "]";
	}
}
