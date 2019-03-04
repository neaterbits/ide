package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.actions.BuiltinAction;

public class IDEMenus {

	public static Menus makeMenues() {
		
		final MenuBuilder builder = new MenuBuilder();
		
		builder
			.addSubMenu(BuiltinMenu.FILE, b -> b
					.addBuiltinAction(BuiltinAction.NEW_POPUP))
			
			.addSubMenu(BuiltinMenu.EDIT, b -> b
					.addBuiltinAction(BuiltinAction.CUT)
					.addBuiltinAction(BuiltinAction.COPY)
					.addBuiltinAction(BuiltinAction.PASTE));

		return new Menus(builder.build());
	}
}
