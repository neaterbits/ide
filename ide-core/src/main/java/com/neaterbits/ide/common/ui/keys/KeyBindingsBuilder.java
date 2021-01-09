package com.neaterbits.ide.common.ui.keys;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.ide.common.ui.actions.BuiltinAction;

final class KeyBindingsBuilder {

	private final List<KeyBinding> keyBindings;
	
	KeyBindingsBuilder() {
		this.keyBindings = new ArrayList<>();
	}
	
	KeyBindingsBuilder addBuiltinAction(BuiltinAction builtinAction, Key key, QualifierKey ... qualifierKeys) {
		keyBindings.add(new KeyBinding(builtinAction.getAction(), key, new KeyMask(qualifierKeys)));
	
		return this;
	}

	KeyBindingsBuilder addBuiltinAction(BuiltinAction builtinAction, char character, QualifierKey ... qualifierKeys) {
		keyBindings.add(new KeyBinding(builtinAction.getAction(), new Key(character), new KeyMask(qualifierKeys)));
	
		return this;
	}
	
	KeyBindings build() {
		return new KeyBindings(keyBindings);
	}
}
