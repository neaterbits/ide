package com.neaterbits.ide.swt;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.model.text.util.StringText;

final class SWTTextEditorView extends SWTBaseTextEditorView {

	private final Text textWidget;
	private TextDiffer textDiffer;

	private com.neaterbits.ide.common.ui.model.text.Text currentText;
	
	SWTTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {

		super(composite, config, sourceFile);

		this.textWidget = new Text(composite, SWT.MULTI|SWT.BORDER);
		
		configure(textWidget);
	}

	@Override
	void setTextModel(BaseTextModel textModel) {
		super.setTextModel(textModel);

		final com.neaterbits.ide.common.ui.model.text.Text text = textModel.getText();
		
		this.currentText = text;
		this.textDiffer = new TextDiffer(currentText);
		
		textWidget.setText(text.asString());
	}

	@Override
	public String getText() {
		return textWidget.getText();
	}

	@Override
	void setText(String text) {
		textWidget.setText(text);
	}

	@Override
	int getCursorPos() {
		return textWidget.getCaretPosition();
	}

	@Override
	void setCursorPos(int pos) {
		textWidget.setSelection(pos);
	}
	
	@Override
	void addKeyListener(KeyListener keyListener) {
		textWidget.addKeyListener(keyListener);
	}
	
	@Override
	void addTextChangeListener(Consumer<ReplaceTextRange> listener) {

		textWidget.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				final ReplaceTextRange replaceTextRange = textDiffer.computeReplaceTextRange(new StringText(textWidget.getText()));

				if (replaceTextRange != null) {
					listener.accept(replaceTextRange);
				}
			}
		});
		
	}

	@Override
	void setFocus() {
		textWidget.setFocus();
	}

	@Override
	void setTabs(int tabs) {
		textWidget.setTabs(tabs);
	}
}
