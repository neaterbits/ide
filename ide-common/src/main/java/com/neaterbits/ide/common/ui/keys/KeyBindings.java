package com.neaterbits.ide.common.ui.keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.ide.common.ui.actions.Action;

public final class KeyBindings {

	private final List<KeyBinding> bindings;
	
	KeyBindings(Collection<KeyBinding> bindings) {
	
		this.bindings = new ArrayList<>(bindings);
	}

	public Action findAction(Key key, KeyMask qualifierMask) {
		
		final KeyCombination keyCombination = new KeyCombination(key, qualifierMask);
	
		for (KeyBinding binding : bindings) {
			
			if (binding.getKeyCombination().matches(keyCombination)) {
				return binding.getAction();
			}
		}
		
		return null;
	}
}
