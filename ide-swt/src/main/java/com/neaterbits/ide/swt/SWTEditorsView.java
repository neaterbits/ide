package com.neaterbits.ide.swt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;

public final class SWTEditorsView implements EditorsView {

	private final TextEditorConfig config;
	private final TabFolder tabFolder;
	
	// private final Composite composite;
	
	private final Map<SourceFileResourcePath, SWTEditorView> editorViews;
	
	public SWTEditorsView(Composite composite, TextEditorConfig config) {
		
		this.config = config;
		this.tabFolder = new TabFolder(composite, SWT.NONE);
		
		// this.composite = new Composite(composite, SWT.NONE);
		
		// this.composite.setLayout(new FillLayout());

		this.editorViews = new HashMap<>();
	}
	
	public TabFolder getTabFolder() {
		return tabFolder;
	}

	/*
	public Composite getComposite() {
		return composite;
	}
	*/

	@Override
	public EditorView displayFile(SourceFileResourcePath sourceFile, BaseTextModel textModel) {

		Objects.requireNonNull(sourceFile);
		Objects.requireNonNull(textModel);
		
		SWTEditorView editorView = editorViews.get(sourceFile);
		
		if (editorView == null) {
				
			editorView = new SWTTextEditorView(this.tabFolder, config, sourceFile);
	
			editorView.setTextModel(textModel);
			
			editorViews.put(sourceFile, editorView);
		}
		
		editorView.setSelectedAndFocused();
		
		return editorView;
	}
	
	@Override
	public SourceFileResourcePath getCurrentEditedFile() {
		
		final int selectionIndex = tabFolder.getSelectionIndex();
		
		final SourceFileResourcePath file;
		
		if (selectionIndex < 0) {
			file = null;
		}
		else {
			file = (SourceFileResourcePath)tabFolder.getItem(selectionIndex).getData();
			
			if (file == null) {
				throw new IllegalStateException();
			}
		}

		return file;
	}

	@Override
	public void closeFile(SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		final SWTEditorView editorView = editorViews.get(sourceFile);

		if (editorView != null) {
			
			try {
				editorView.close();
			}
			finally {
				editorViews.remove(editorView);
			}
		}
	}
}
