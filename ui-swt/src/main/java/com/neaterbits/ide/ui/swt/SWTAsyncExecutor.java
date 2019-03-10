package com.neaterbits.ide.ui.swt;

import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.util.scheduling.BaseAsyncExecutor;

public final class SWTAsyncExecutor extends BaseAsyncExecutor<Void> {

	public SWTAsyncExecutor(Display display) {
		super(
				runnable -> display.asyncExec(() -> {
					
					try {
						runnable.run();
					}
					catch (Throwable ex) {
						ex.printStackTrace();
					}
				}),
				true);
	}
}
