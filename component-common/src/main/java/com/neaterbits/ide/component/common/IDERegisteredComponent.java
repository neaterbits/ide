package com.neaterbits.ide.component.common;

import java.util.Objects;

final class IDERegisteredComponent {

	private final IDEComponent component;
	private final UIComponentProvider uiComponentProvider;

	IDERegisteredComponent(IDEComponent component, UIComponentProvider uiComponentProvider) {
		
		Objects.requireNonNull(component);
		
		this.component = component;
		this.uiComponentProvider = uiComponentProvider;
	}

	IDEComponent getComponent() {
		return component;
	}

	UIComponentProvider getUiComponentProvider() {
		return uiComponentProvider;
	}
}
