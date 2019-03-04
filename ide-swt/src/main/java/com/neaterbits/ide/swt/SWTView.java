package com.neaterbits.ide.swt;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.neaterbits.ide.common.ui.view.View;

abstract class SWTView {

	private static final String KEY_IDEVIEW = "ideview";
	
	
	static View findSelectedView(Widget widget) {
		
		for (Control control = (Control)widget; control != null; control = control.getParent()) {
			
			final Object data = control.getData(KEY_IDEVIEW);
			
			if (data != null) {
				return (View)data;
			}
		}
		
		return null;
	}
	
	final void setIDEView(View view, Control control) {
		
		control.setData(KEY_IDEVIEW, view);
		
	}
}
