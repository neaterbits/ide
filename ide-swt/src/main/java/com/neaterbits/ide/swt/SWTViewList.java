package com.neaterbits.ide.swt;

import java.util.Objects;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.common.ui.view.ViewList;

final class SWTViewList extends ViewList {

	private static final String KEY_IDEVIEW = "ideview";

	void addView(View impl, Control control) {
		
		Objects.requireNonNull(impl);

		final View view = super.addView(impl);
		
		control.setData(KEY_IDEVIEW, view);
		
		control.addDisposeListener(event -> removeView(impl));
	}
	
	static View findView(Widget widget) {
		
		for (Control control = (Control)widget; control != null; control = control.getParent()) {
			
			final Object data = control.getData(KEY_IDEVIEW);
			
			if (data != null) {
				return (View)data;
			}
		}
		
		return null;
	}
}
