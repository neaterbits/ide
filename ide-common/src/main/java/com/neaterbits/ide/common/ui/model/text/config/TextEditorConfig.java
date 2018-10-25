package com.neaterbits.ide.common.ui.model.text.config;

public class TextEditorConfig {

	private final int tabs;
	private final boolean tabsToSpaces;

	public TextEditorConfig(int tabs, boolean tabsToSpaces) {
		this.tabs = tabs;
		this.tabsToSpaces = tabsToSpaces;
	}
	public int getTabs() {
		return tabs;
	}
	public boolean isTabsToSpaces() {
		return tabsToSpaces;
	}
}
