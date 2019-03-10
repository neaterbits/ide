package com.neaterbits.ide.common.ui;

import com.neaterbits.ide.common.ui.controller.UIParameters;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.view.MapMenuItem;
import com.neaterbits.ide.common.ui.view.SystemClipboard;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.util.scheduling.ForwardResultToCaller;

public interface UI {

	ForwardResultToCaller getIOForwardToCaller();
	
	SystemClipboard getSystemClipboard();
	
	UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem);

	void runInitialEvents();

	void main();
	
	void addFocusListener(ViewFocusListener focusListener);
	
}
