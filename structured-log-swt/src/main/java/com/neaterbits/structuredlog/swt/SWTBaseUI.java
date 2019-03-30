package com.neaterbits.structuredlog.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

abstract class SWTBaseUI {

	final Shell window;
	
	SWTBaseUI() {
		this.window = new Shell();
	}

	final void start() {

		final Display display = window.getDisplay();
		
		while (display.readAndDispatch() && !window.isDisposed()) {
			display.sleep();
		}
	}
}
