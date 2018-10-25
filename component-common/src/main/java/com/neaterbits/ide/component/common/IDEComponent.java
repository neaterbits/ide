package com.neaterbits.ide.component.common;

import java.util.Objects;

final class IDEComponent<WINDOW> {

	private final ComponentProvider componentProvider;
	private final UIComponentProvider<WINDOW> uiComponentProvider;

	public IDEComponent(ComponentProvider componentProvider, UIComponentProvider<WINDOW> uiComponentProvider) {
		
		Objects.requireNonNull(componentProvider);
		
		this.componentProvider = componentProvider;
		this.uiComponentProvider = uiComponentProvider;
	}

	public ComponentProvider getComponentProvider() {
		return componentProvider;
	}

	public UIComponentProvider<WINDOW> getUiComponentProvider() {
		return uiComponentProvider;
	}
}
