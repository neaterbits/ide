package com.neaterbits.ide.common.ui.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.ide.common.ui.actions.BuiltinAction;

final class MenuBuilder {

	private final List<MenuEntry> entries;
	
	MenuBuilder() {
		this.entries = new ArrayList<>();
	}
	
	MenuBuilder addSubMenu(BuiltinMenu name, Consumer<MenuBuilder> builder) {
	
		final MenuBuilder subBuilder = new MenuBuilder();
		
		builder.accept(subBuilder);

		final List<MenuEntry> subEntries = subBuilder.build();
		
		entries.add(new SubMenuEntry(name, subEntries));
		
		return this;
	}

	MenuBuilder addBuiltinAction(BuiltinAction action) {
		
		entries.add(new BuiltinActionMenuEntry(action));
		
		return this;
	}
	
	List<MenuEntry> build() {
		return entries;
	}
}
