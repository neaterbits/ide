package com.neaterbits.ide.swt;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;

final class SWTStyledTextEditorView extends SWTBaseTextEditorView {

	private final StyledText textWidget;
	
	SWTStyledTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {
		super(composite, config, sourceFile);

		this.textWidget = new StyledText(composite, SWT.NONE);
		
		configure(textWidget);
	}

	
	@Override
	void setTextModel(BaseTextModel textModel) {
		super.setTextModel(textModel);
		
		setText(textModel.getText());
	}



	@Override
	public String getText() {
		return textWidget.getText();
	}

	@Override
	void setText(String text) {
		this.textWidget.setText(text);
	}

	@Override
	void setCursorPos(int pos) {
		textWidget.setCaretOffset(pos);
	}

	@Override
	int getCursorPos() {
		return textWidget.getCaretOffset();
	}

	@Override
	void setFocus() {
		textWidget.setFocus();
	}

	@Override
	void setTabs(int tabs) {
		textWidget.setTabs(tabs);
	}

	@Override
	void addKeyListener(KeyListener keyListener) {
		textWidget.addKeyListener(keyListener);
	}

	@Override
	void addTextChangeListener(Consumer<ReplaceTextRange> listener) {
		textWidget.addExtendedModifyListener(event -> listener.accept(new ReplaceTextRange(event.start, event.length, event.replacedText)));
	}
}
