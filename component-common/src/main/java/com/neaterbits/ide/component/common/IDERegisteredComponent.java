package com.neaterbits.ide.component.common;

import java.util.Objects;

final class IDERegisteredComponent {

	private final ComponentProvider componentProvider;
	private final UIComponentProvider uiComponentProvider;

	IDERegisteredComponent(ComponentProvider componentProvider, UIComponentProvider uiComponentProvider) {
		
		Objects.requireNonNull(componentProvider);
		
		this.componentProvider = componentProvider;
		this.uiComponentProvider = uiComponentProvider;
	}

	ComponentProvider getComponentProvider() {
		return componentProvider;
	}

	UIComponentProvider getUiComponentProvider() {
		return uiComponentProvider;
	}
}
