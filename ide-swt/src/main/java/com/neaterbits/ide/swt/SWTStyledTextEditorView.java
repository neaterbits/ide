package com.neaterbits.ide.swt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jface.resource.DeviceResourceManager;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.CursorPositionListener;
import com.neaterbits.ide.common.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.TextEditorChangeListener;
import com.neaterbits.ide.common.ui.view.TextSelectionListener;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.util.ui.RGBColor;
import com.neaterbits.ide.util.ui.text.StringText;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;
import com.neaterbits.ide.util.ui.text.styling.TextStylingModel;

final class SWTStyledTextEditorView extends SWTBaseTextEditorView {

	private final StyledText textWidget;
	
	private final LocalResourceManager resourceManager;
	private final Map<RGBColor, Color> swtColors;
	
	private TextModel textModel;
	
	SWTStyledTextEditorView(
			SWTViewList viewList,
			TabFolder composite,
			TextEditorConfig config,
			TextStylingModel textStylingModel,
			SourceFileResourcePath sourceFile,
			EditorSourceActionContextProvider editorSourceActionContextProvider) {
		
		super(composite, config, sourceFile, editorSourceActionContextProvider);

		this.textWidget = new StyledText(composite, SWT.NONE);
	
		this.resourceManager = new LocalResourceManager(new DeviceResourceManager(composite.getDisplay()));
		this.swtColors = new HashMap<>();
		
		final Font font = resourceManager.createFont(new SingleFontDescriptor("Monospace", 10, SWT.NONE));
		
		textWidget.addDisposeListener(event -> resourceManager.dispose());
		textWidget.setFont(font);

		if (textStylingModel != null) {
			textWidget.addLineStyleListener(event -> {
				
				final Collection<TextStyleOffset> offsets = textStylingModel.getLineStyleOffsets(event.lineOffset, event.lineText.length(), new StringText(event.lineText));
				
				event.styles = makeStyleRanges(offsets, resourceManager);
				
			});
		}
		
		configure(textWidget);

		viewList.addView(this, textWidget);
	}

	@Override
	public Text getText() {
		return new StringText(textWidget.getText());
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
	public TextRange getSelection() {
		final Point selection = textWidget.getSelection();
		
		return selection != null
				? new TextRange(selection.x,  selection.y - selection.x + 1)
				: null; 
	}

	@Override
	public void addTextChangeListener(TextEditorChangeListener listener) {
		
		textWidget.addExtendedModifyListener(event -> {
			System.out.println("## text change");
		
			listener.onTextChange(event.start, event.length, event.replacedText != null ? new StringText(event.replacedText) : null);
		});
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
	public void addKeyListener(KeyEventListener listener) {

		final SWTKeyEventListener keyEventListener = new SWTKeyEventListener(listener);
		
		textWidget.addKeyListener(keyEventListener);
		
		textWidget.addVerifyKeyListener(new VerifyKeyListener() {
			
			@Override
			public void verifyKey(VerifyEvent event) {
				event.doit = keyEventListener.keyPressed(event.character, event.keyCode, event.keyLocation, event.stateMask);
			}
		});
	}

	
	@Override
	public void setTextModel(TextModel textModel) {

		Objects.requireNonNull(textModel);

		this.textModel = textModel;

		textWidget.setContent(new StyledTextContent() {
			
			@Override
			public void setText(String text) {
				textModel.setText(new StringText(text));;
			}
			
			@Override
			public void replaceTextRange(int start, int length, String replaceText) {
				textModel.replaceTextRange(start, length, new StringText(replaceText));
			}
			
			@Override
			public void removeTextChangeListener(TextChangeListener arg0) {
				
			}
			
			@Override
			public String getTextRange(int start, int length) {
				return textModel.getTextRange(start, length).asString();
			}
			
			@Override
			public int getOffsetAtLine(int lineIndex) {
				return (int)textModel.getOffsetAtLine(lineIndex);
			}
			
			@Override
			public String getLineDelimiter() {
				return textModel.getLineDelimiter().asString();
			}
			
			@Override
			public int getLineCount() {
				return (int)textModel.getLineCount();
			}
			
			@Override
			public int getLineAtOffset(int offset) {
				return (int)textModel.getLineAtOffset(offset);
			}
			
			@Override
			public String getLine(int lineIndex) {
				return textModel.getLineWithoutAnyNewline(lineIndex).asString();
			}
			
			@Override
			public int getCharCount() {
				return (int)textModel.getCharCount();
			}
			
			@Override
			public void addTextChangeListener(TextChangeListener arg0) {
				
			}
		});
	}

	@Override
	public void triggerTextRefresh() {

		
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
	public void select(long offset, long length) {
		textWidget.setSelection((int)offset, (int)(offset + length));
	}

	@Override
	public void triggerStylingRefresh() {

		if (textModel != null) {
			final int cursorPosition = getCursorPos();
			
			setTextModel(textModel);
			
			setCursorPos(cursorPosition);
		}
	}

	@Override
	boolean hasSelectedText() {
		return textWidget.isTextSelected();
	}
	

	private Color getColor(RGBColor rgb) {
		
		Color color = swtColors.get(rgb);
		
		if (color == null) {
			color = resourceManager.createColor(new RGB(
					rgb.getR(),
					rgb.getG(),
					rgb.getB()));
			
			swtColors.put(rgb, color);
			
		}
		
		return color;
	}
	
	private StyleRange [] makeStyleRanges(Collection<TextStyleOffset> styles, ResourceManager resourceManager) {

		final StyleRange [] result = new StyleRange[styles.size()];
		
		int dstIdx = 0;
		
		for (TextStyleOffset style : styles) {
			
			result[dstIdx ++] = new StyleRange(
					(int)style.getStart(),
					(int)style.getLength(),
					getColor(style.getColor()),
					null);
			
		}
		
		return result;
	}
}
