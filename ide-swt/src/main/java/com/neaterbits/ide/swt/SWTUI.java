package com.neaterbits.ide.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.neaterbits.ide.common.ui.UI;
import com.neaterbits.ide.common.ui.model.ProjectModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.UIView;
import com.neaterbits.ide.util.scheduling.ForwardToCaller;

public class SWTUI implements UI<Shell> {

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
	public UIView<Shell> makeUIView(ProjectModel projectModel, TextEditorConfig config) {
		return new SWTUIView(display, projectModel, config);
	}

	@Override
	public void main() {

		while (display.readAndDispatch()) {
			display.sleep();
		}
	}
}
