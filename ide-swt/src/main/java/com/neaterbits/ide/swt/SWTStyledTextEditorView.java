package com.neaterbits.ide.swt;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.TextChangeListener;
import com.neaterbits.ide.util.ui.text.StringText;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class SWTStyledTextEditorView extends SWTBaseTextEditorView {

	private final StyledText textWidget;
	
	private int textChangeEventsSinceSetWidgetText = 0;
	
	SWTStyledTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {
		super(composite, config, sourceFile);

		this.textWidget = new StyledText(composite, SWT.NONE);
		
		final Font font = new Font(composite.getDisplay(), new FontData("Monospace", 10, SWT.NONE));
		
		textWidget.addDisposeListener(event -> font.dispose());
		
		textWidget.setFont(font);
		
		configure(textWidget);
	}
	
	
	@Override
	public void setCurrentText(Text text) {
		setWidgetText(text.asString());
	}

	@Override
	public Text getText() {
		return new StringText(textWidget.getText());
	}

	@Override
	void setWidgetText(String text) {
		
		this.textChangeEventsSinceSetWidgetText = 0;
		
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
	public void addTextChangeListener(TextChangeListener listener) {
		
		if (textChangeEventsSinceSetWidgetText > 0) {
			textWidget.addExtendedModifyListener(event -> listener.onTextChange(event.start, event.length, new StringText(event.replacedText)));
		}
		
		++ textChangeEventsSinceSetWidgetText;
	}

	@Override
	public void applyStyles(Collection<TextStyleOffset> styles) {

		for (TextStyleOffset style : styles) {
			final StyleRange styleRange = new StyleRange(
					style.getStart(),
					style.getLength(),
					new Color(
							null,
							style.getColor().getR(),
							style.getColor().getG(),
							style.getColor().getB()),
					null);
			
			System.out.println("## apply style range " + styleRange);
			
			textWidget.setStyleRange(styleRange);
		}
	}
}
