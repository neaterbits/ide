package com.neaterbits.ide.swt;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.CursorPositionListener;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.TextEditorChangeListener;
import com.neaterbits.ide.common.ui.view.TextSelectionListener;
import com.neaterbits.ide.core.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.ui.swt.SWTViewList;
import com.neaterbits.ide.ui.swt.SWTStyledText;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

final class SWTStyledTextEditorView extends SWTBaseTextEditorView {
    
    private SWTStyledText styled;

	SWTStyledTextEditorView(
			SWTViewList viewList,
			TabFolder composite,
			TextEditorConfig config,
			TextStylingModel textStylingModel,
			SourceFileResourcePath sourceFile,
			EditorSourceActionContextProvider editorSourceActionContextProvider) {
		
		super(composite, config, sourceFile, editorSourceActionContextProvider);

		this.styled = new SWTStyledText(composite);
		
		if (textStylingModel != null) {
		    styled.setStylingModel(textStylingModel);
		}
		
		configure(styled.getTextWidget());

		viewList.addView(this, styled.getTextWidget());
	}

	@Override
	public Text getText() {
		return styled.getText();
	}

	@Override
	void setCursorPos(int pos) {
		styled.setCursorPos(pos);
	}

	@Override
	int getCursorPos() {
		return styled.getCursorPos();
	}

	@Override
	void setFocus() {
	    styled.setFocus();
	}

	@Override
	void setTabs(int tabs) {
	    styled.setTabs(tabs);
	}
	
	@Override
	void addKeyListener(KeyListener keyListener) {
		styled.addKeyListener(keyListener);
	}
	
	@Override
	public TextRange getSelection() {
	    return styled.getSelection();
	}

	@Override
	public void addTextChangeListener(TextEditorChangeListener listener) {
	    styled.addTextChangeListener(listener);
	}
	
	@Override
	void addTextSelectionListener(TextSelectionListener textSelectionListener) {
	    styled.addTextSelectionListener(textSelectionListener);
	}

	@Override
	public void addCursorPositionListener(CursorPositionListener cursorPositionListener) {
	    styled.addCursorPositionListener(cursorPositionListener);
	}

	@Override
	public void addKeyListener(KeyEventListener listener) {
	    styled.addKeyListener(listener);
	}
	
	@Override
	public void setTextModel(TextModel textModel) {
	    styled.setTextModel(textModel);
	}

	@Override
	public void triggerTextRefresh() {

	}

	@Override
	public void cut() {
	    styled.cut();
	}

	@Override
	public void copy() {
	    styled.copy();
	}

	@Override
	public void paste() {
	    styled.paste();
	}

	@Override
	public void selectAll() {
	    styled.selectAll();
	}
	
	@Override
	public void select(long offset, long length) {
	    styled.select(offset, length);
	}

	@Override
	public void triggerStylingRefresh() {
	    styled.triggerStylingRefresh();
	}

	@Override
	boolean hasSelectedText() {
		return styled.hasSelectedText();
	}
	
}
