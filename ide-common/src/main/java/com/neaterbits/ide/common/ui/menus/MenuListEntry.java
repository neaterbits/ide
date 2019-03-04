package com.neaterbits.ide.common.ui.menus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class MenuListEntry extends MenuEntry {

	private final List<MenuEntry> entries;

	MenuListEntry(List<MenuEntry> entries) {

		Objects.requireNonNull(entries);
		
		this.entries = entries;
	}

	public final List<MenuEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [entries=" + entries + "]";
	}

}
