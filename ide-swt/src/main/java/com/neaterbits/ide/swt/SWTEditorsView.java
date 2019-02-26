package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorView;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.common.ui.view.KeyEventListener;

public final class SWTEditorsView implements EditorsView {

	private final TextEditorConfig config;
	private final TabFolder tabFolder;
	
	// private final Composite composite;
	
	private final List<SWTEditorView> editorViews;
	
	public SWTEditorsView(Composite composite, TextEditorConfig config) {
		
		this.config = config;
		this.tabFolder = new TabFolder(composite, SWT.NONE);
		
		// this.composite = new Composite(composite, SWT.NONE);
		
		// this.composite.setLayout(new FillLayout());

		this.editorViews = new ArrayList<>();
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

		final SWTEditorView editorView = new SWTTextEditorView(this.tabFolder, config, sourceFile);

		editorView.setTextModel(textModel);

		editorView.setFocused();
		
		editorViews.add(editorView);
		
		return editorView;
	}
	
	@Override
	public SourceFileResourcePath getCurrentEditedFile() {
		final int selectionIndex = tabFolder.getSelectionIndex();
		
		return selectionIndex < 0 ? null : editorViews.get(selectionIndex).getSourceFile();
	}

	@Override
	public void closeFile(SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		final SWTEditorView editorView = editorViews.stream()
			.filter(view -> view.getSourceFile().equals(sourceFile))
			.findFirst()
			.orElse(null);

		if (editorView != null) {
			editorView.close();
			
			if (!editorViews.remove(editorView)) {
				throw new IllegalStateException();
			}
		}
	}
}
