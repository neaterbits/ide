package com.neaterbits.ide.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.neaterbits.ide.common.ui.UI;
import com.neaterbits.ide.common.ui.ViewFocusListener;
import com.neaterbits.ide.common.ui.controller.UIParameters;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.view.MapMenuItem;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public class SWTUI implements UI {

	private final Display display;
	
	public SWTUI() {
	
		this.display = Display.getDefault();

	}
	
	@Override
	public ForwardToCaller getIOForwardToCaller() {
		return new ForwardToCaller() {
			
			@Override
			public void forward(Runnable runnable) {
				display.asyncExec(runnable);
			}
		};
	}
	

	@Override
	public UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem) {
	
		return new SWTUIView(display, uiParameters, menus, mapMenuItem);
	}

	private View focusedView = null;
	
	@Override
	public void addFocusListener(ViewFocusListener focusListener) {
		display.addFilter(SWT.FocusIn, new Listener() {
			
			@Override
			public void handleEvent(Event event) {

				focusedView = SWTViewList.findView(event.widget);
				
				if (focusedView != null) {
					focusListener.onViewFocusChange(focusedView);
				}
			}
		});

		display.addFilter(SWT.FocusOut, new Listener() {
			
			@Override
			public void handleEvent(Event event) {

				final View view = SWTViewList.findView(event.widget);
				
				if (view != null) {
					
					if (view != focusedView) {
						throw new IllegalStateException();
					}
					
					focusedView = null;

					focusListener.onViewFocusChange(null);
				}
			}
		});
	}

	@Override
	public void main() {

		while (display.readAndDispatch()) {
			display.sleep();
		}
	}
}
