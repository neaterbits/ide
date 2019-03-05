package com.neaterbits.ide.common.ui.keys;

import com.neaterbits.ide.common.ui.actions.BuiltinAction;

public class IDEKeyBindings {

	public static KeyBindings makeKeyBindings() {
	
		final KeyBindingsBuilder builder = new KeyBindingsBuilder();
		
		builder
			
			.addBuiltinAction(BuiltinAction.NEW_DIALOG, 'n', QualifierKey.CTRL)
			
			.addBuiltinAction(BuiltinAction.CUT, 'x', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.COPY, 'c', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.PASTE, 'v', QualifierKey.CTRL)
			
			.addBuiltinAction(BuiltinAction.OPEN_TYPE, 't', QualifierKey.CTRL, QualifierKey.SHIFT)
			.addBuiltinAction(BuiltinAction.SHOW_IN_PROJECTS, 'w', QualifierKey.ALT, QualifierKey.SHIFT)
			.addBuiltinAction(BuiltinAction.CLOSE_EDITED,  'w', QualifierKey.CTRL)
			.addBuiltinAction(BuiltinAction.MIN_MAX_EDITORS, 'm', QualifierKey.CTRL);
		
		return builder.build();
	}
	
}
