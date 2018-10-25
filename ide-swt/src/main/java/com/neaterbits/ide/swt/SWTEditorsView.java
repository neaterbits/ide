package com.neaterbits.ide.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;

public final class SWTEditorsView implements EditorsView {

	// private final TabFolder tabFolder;
	
	private final Composite composite;
	
	private final SWTEditorView editorView;
	

	public SWTEditorsView(Composite composite, TextEditorConfig config) {
		
		// this.tabFolder = new TabFolder(composite, SWT.NONE);
		
		this.composite = new Composite(composite, SWT.NONE);
		
		this.composite.setLayout(new FillLayout());

		this.editorView = new SWTTextEditorView(this.composite, config);
	}
	
	/*
	public TabFolder getTabFolder() {
		return tabFolder;
	}
	*/

	public Composite getComposite() {
		return composite;
	}

	@Override
	public EditorView displayFile(String fileName, BaseTextModel textModel) {

		editorView.setTextModel(textModel);
		
		editorView.setFocused();
		
		return editorView;
	}

	@Override
	public void closeFile() {
		editorView.close();
	}
}
