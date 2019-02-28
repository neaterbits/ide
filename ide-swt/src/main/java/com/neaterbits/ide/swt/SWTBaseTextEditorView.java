package com.neaterbits.ide.swt;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.util.ui.text.Text;

abstract class SWTBaseTextEditorView extends SWTEditorView {

	private final TabFolder composite;
	private final TabItem tabItem;

	private TextEditorConfig config;
	
	private final SourceFileResourcePath sourceFile;

	abstract void setWidgetText(String text);
	abstract void setCursorPos(int pos);
	abstract int getCursorPos();
	abstract void setFocus();
	abstract void setTabs(int tabs);
	abstract void addKeyListener(KeyListener keyListener);
	
	SWTBaseTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {

		this.composite = composite;
		
		this.sourceFile = sourceFile;
		
		this.tabItem = new TabItem(composite, SWT.NONE);

		tabItem.setText(sourceFile.getName());
		tabItem.setData(sourceFile);
		
		Objects.requireNonNull(config);
		
		this.config = config;
	}
	
	final void configure(Control tabItemControl) {
		
		Objects.requireNonNull(tabItemControl);
		
		tabItem.setControl(tabItemControl);
		
		composite.setSelection(tabItem);

		configure(config);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				if ((e.stateMask & SWT.SHIFT) != 0) {
					if (e.keyLocation == SWT.KEYPAD) {
						e.doit = false;
					}
				}
				else if (SWTBaseTextEditorView.this.config.isTabsToSpaces() && e.keyCode == SWT.TAB) {
					
					// System.out.println("position: " + textWidget.getCaretPosition());
					
					final Text text = getText();
					
					final int cursorPos = getCursorPos();
					
					final String spaces = Strings.spaces(SWTBaseTextEditorView.this.config.getTabs());
					
					final String updatedText = Strings.replaceTextRange(
							text.asString(),
							cursorPos,
							0,
							spaces);
					
					setWidgetText(updatedText);
					
					setCursorPos(cursorPos + spaces.length());
					
					e.doit = false;
				}
				else if (e.character == '\r') {
					
					int cursorPos = getCursorPos();
					
					final String text = getText().asString();
				
					indentOnNewline(text, cursorPos);
					
					e.doit = false;
				}
			}
		});
	}

	private void indentOnNewline(String text, int cursorPos) {
		int lineStart;
		
		if (cursorPos == 0) {
			lineStart = 0;
		}
		else if (text.charAt(cursorPos) == '\n') {
			lineStart = text.lastIndexOf("\n", cursorPos - 1);
		}
		else {
			lineStart = text.lastIndexOf("\n", cursorPos);
		}
		
		if (lineStart < 0) {
			lineStart = 0;
		}
		else {
			++ lineStart;
		}
		
		System.out.println("## linestart: " + lineStart + ", caretPosition " + cursorPos);
		
		final String line = text.substring(lineStart, cursorPos);
		
		
		System.out.println("### line \"" + line + "\"");
		
		final int addIndent;
		
		if (line.trim().endsWith("{")) {
			addIndent = 1;
		}
		else {
			addIndent = 0;
		}
		
		addIndentedNewline(cursorPos, line, addIndent);
	}

	private void addIndentedNewline(int caretPosition, String line, int addIndent) {
		int numWhitespace = 0;

		final int tabs = config.getTabs();
		
		for (int i = 0; i < line.length(); ++ i) {
			final char c = line.charAt(i);
			
			if (Character.isWhitespace(c)) {
				switch (c) {
				case ' ':
					++ numWhitespace;
					break;
					
				case '\t':
					numWhitespace += tabs;
					break;
					
				default:
					throw new UnsupportedOperationException("Unknown whitespace character '" + c + "'");
				}
			}
			else {
				break;
			}
		}
		
		System.out.println("Add whitespace: " + numWhitespace);
		
		final int numIndent = numWhitespace / tabs;
		
		final String newlineIndentation;
		
		if (config.isTabsToSpaces()) {
			newlineIndentation = Strings.spaces((numIndent + addIndent) * tabs);
		}
		else {
			newlineIndentation = Strings.concat("\t", numIndent + addIndent);
		}
		
		System.out.println("### add newline indentation \"" + newlineIndentation + "\"");
		
		final String newline = "\n" + newlineIndentation;
		
		final String updatedText = Strings.replaceTextRange(
				getText().asString(),
				caretPosition,
				0,
				newline);

		setWidgetText(updatedText);
		
		setCursorPos(caretPosition + newline.length());
	}

	@Override
	final SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	@Override
	final void setSelectedAndFocused() {
		composite.setSelection(tabItem);
		
		setFocus();
	}

	@Override
	final void close() {
		tabItem.dispose();
	}

	@Override
	final void configure(TextEditorConfig config) {
		
		Objects.requireNonNull(config);
		
		setTabs(config.getTabs());
		
		this.config = config;
	}
}
