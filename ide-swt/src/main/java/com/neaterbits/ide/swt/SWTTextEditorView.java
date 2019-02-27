package com.neaterbits.ide.swt;

import java.util.Collection;
import java.util.Objects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.TextChangeListener;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class SWTTextEditorView extends SWTBaseTextEditorView {

	private final Text textWidget;
	private TextDiffer textDiffer;

	private String currentText;
	
	SWTTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {

		super(composite, config, sourceFile);

		this.textWidget = new Text(composite, SWT.MULTI|SWT.BORDER);
		
		configure(textWidget);
	}

	@Override
	public void setCurrentText(String text) {
		
		Objects.requireNonNull(text);
		
		this.currentText = text;
		this.textDiffer = new TextDiffer(currentText);
		
		textWidget.setText(text);
	}

	@Override
	public String getText() {
		return textWidget.getText();
	}

	@Override
	void setWidgetText(String text) {
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
	public void addTextChangeListener(TextChangeListener listener) {
		
		textWidget.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				final ReplaceTextRange replaceTextRange = textDiffer.computeReplaceTextRange(textWidget.getText());

				if (replaceTextRange != null) {
					listener.onTextChange(replaceTextRange.getStart(), replaceTextRange.getStart(), replaceTextRange.getText());
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

	@Override
	public void applyStyles(Collection<TextStyleOffset> styles) {
		
	}
}
