package com.neaterbits.ide.common.ui;

import com.neaterbits.ide.common.ui.controller.UIParameters;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.view.MapMenuItem;
import com.neaterbits.ide.common.ui.view.SystemClipboard;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public interface UI {

	ForwardToCaller getIOForwardToCaller();
	
	SystemClipboard getSystemClipboard();
	
	UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem);
	
	void main();
	
	void addFocusListener(ViewFocusListener focusListener);
	
}
