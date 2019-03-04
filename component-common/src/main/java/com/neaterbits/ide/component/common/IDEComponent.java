package com.neaterbits.ide.component.common;

import java.util.Objects;

final class IDEComponent {

	private final ComponentProvider componentProvider;
	private final UIComponentProvider uiComponentProvider;

	public IDEComponent(ComponentProvider componentProvider, UIComponentProvider uiComponentProvider) {
		
		Objects.requireNonNull(componentProvider);
		
		this.componentProvider = componentProvider;
		this.uiComponentProvider = uiComponentProvider;
	}

	public ComponentProvider getComponentProvider() {
		return componentProvider;
	}

	public UIComponentProvider getUiComponentProvider() {
		return uiComponentProvider;
	}
}
