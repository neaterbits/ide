package com.neaterbits.ide.ui.swt;

import java.util.Objects;

import org.eclipse.swt.widgets.Shell;

import com.neaterbits.ide.component.common.UIComponentContext;

public final class SWTUIContext extends UIComponentContext {

	private final Shell window;

	public SWTUIContext(Shell window) {

		Objects.requireNonNull(window);
		
		this.window = window;
	}

	public Shell getWindow() {
		return window;
	}
}
