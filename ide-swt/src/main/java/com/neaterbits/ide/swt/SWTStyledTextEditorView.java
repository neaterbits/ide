package com.neaterbits.ide.swt;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.CursorPositionListener;
import com.neaterbits.ide.common.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.common.ui.view.TextChangeListener;
import com.neaterbits.ide.common.ui.view.TextSelectionListener;
import com.neaterbits.ide.util.ui.text.StringText;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

final class SWTStyledTextEditorView extends SWTBaseTextEditorView {

	private final StyledText textWidget;
	
	private int textChangeEventsSinceSetWidgetText = 0;
	
	SWTStyledTextEditorView(
			SWTViewList viewList,
			TabFolder composite,
			TextEditorConfig config,
			TextStylingModel textStylingModel,
			SourceFileResourcePath sourceFile,
			EditorSourceActionContextProvider editorSourceActionContextProvider) {
		
		super(composite, config, sourceFile, editorSourceActionContextProvider);

		this.textWidget = new StyledText(composite, SWT.NONE);
		
		final Font font = new Font(composite.getDisplay(), new FontData("Monospace", 10, SWT.NONE));
		textWidget.addDisposeListener(event -> font.dispose());
		textWidget.setFont(font);

		if (textStylingModel != null) {
			textWidget.addLineStyleListener(event -> {
				
				final Collection<TextStyleOffset> offsets = textStylingModel.getStyleOffsets(event.lineOffset, event.lineText.length());
				
				event.styles = makeStyleRanges(offsets);
				
			});
		}
		
		configure(textWidget);

		viewList.addView(this, textWidget);
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
	void addTextSelectionListener(TextSelectionListener textSelectionListener) {
		textWidget.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				textSelectionListener.onTextSelectionChange(hasSelectedText());
			}
		});
	}

	@Override
	public void addCursorPositionListener(CursorPositionListener cursorPositionListener) {

		textWidget.addCaretListener(event -> cursorPositionListener.onCursorPositionChanged(event.caretOffset));
		
	}

	@Override
	public void cut() {
		textWidget.cut();
	}

	@Override
	public void copy() {
		textWidget.copy();
	}

	@Override
	public void paste() {
		textWidget.paste();
	}

	@Override
	public void selectAll() {
		textWidget.setSelection(0, textWidget.getCharCount());
	}

	@Override
	boolean hasSelectedText() {
		return textWidget.isTextSelected();
	}

	private static StyleRange [] makeStyleRanges(Collection<TextStyleOffset> styles) {

		final StyleRange [] result = new StyleRange[styles.size()];
		
		int dstIdx = 0;
		
		for (TextStyleOffset style : styles) {
			result[dstIdx ++] = new StyleRange(
					(int)style.getStart(),
					(int)style.getLength(),
					new Color(
							null,
							style.getColor().getR(),
							style.getColor().getG(),
							style.getColor().getB()),
					null);
			
		}
		
		return result;
	}
}
