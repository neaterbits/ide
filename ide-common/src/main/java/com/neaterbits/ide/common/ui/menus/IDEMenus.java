package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.actions.BuiltinAction;
import com.neaterbits.ide.common.ui.keys.KeyBindings;

public class IDEMenus {

	public static Menus makeMenues(KeyBindings keyBindings) {
		
		final MenuBuilder builder = new MenuBuilder(keyBindings);
		
		builder
			.addSubMenu(BuiltinMenu.FILE, b -> b
					.addBuiltinAction(BuiltinAction.NEW_DIALOG))
			
			.addSubMenu(BuiltinMenu.EDIT, b -> b
					.addBuiltinAction(BuiltinAction.CUT)
					.addBuiltinAction(BuiltinAction.COPY)
					.addBuiltinAction(BuiltinAction.PASTE))
					
			.addSubMenu(BuiltinMenu.REFACTOR,  b -> b
					.addBuiltinAction(BuiltinAction.RENAME)
					.addBuiltinAction(BuiltinAction.MOVE))
			.addSubMenu(BuiltinMenu.NAVIGATE, b -> b
					.addBuiltinAction(BuiltinAction.TYPE_HIERARCHY));

		return new Menus(builder.build());
	}
}
