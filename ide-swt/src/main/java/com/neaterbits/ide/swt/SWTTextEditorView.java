package com.neaterbits.ide.swt;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;

final class SWTTextEditorView extends SWTEditorView {

	private final TabFolder composite;
	
	private TextEditorConfig config;
	
	private final SourceFileResourcePath sourceFile;
	
	private final TabItem tabItem;
	
	private final Text textWidget;
	private BaseTextModel textModel;
	private TextDiffer textDiffer;

	private String currentText;
	
	SWTTextEditorView(TabFolder composite, TextEditorConfig config, SourceFileResourcePath sourceFile) {

		this.composite = composite;
		
		this.sourceFile = sourceFile;
		
		this.tabItem = new TabItem(composite, SWT.NONE);

		tabItem.setText(sourceFile.getName());
		tabItem.setData(sourceFile);
		
		Objects.requireNonNull(config);
		
		this.config = config;
		
		this.textWidget = new Text(composite, SWT.MULTI|SWT.BORDER);
		
		tabItem.setControl(textWidget);
		
		composite.setSelection(tabItem);
		
		configure(config);

		// System.out.println("### create editorview");
		
		textWidget.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				onTextChange(textWidget.getText());
			}
		});
		
		textWidget.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				/*
				System.out.println("## key " + e.keyCode);
				System.out.println("## key location " + e.keyLocation);
				System.out.println("## stateMask " + e.stateMask);
				*/

				/*
				if ((e.stateMask & SWT.SHIFT) != 0) {
					System.out.println("## shift pressed");
				}
				*/
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				if ((e.stateMask & SWT.SHIFT) != 0) {
					if (e.keyLocation == SWT.KEYPAD) {
						e.doit = false;
					}
				}
				else if (SWTTextEditorView.this.config.isTabsToSpaces() && e.keyCode == SWT.TAB) {
					
					// System.out.println("position: " + textWidget.getCaretPosition());
					
					final String text = textWidget.getText();
					
					final int caretPosition = textWidget.getCaretPosition();
					
					final String spaces = Strings.spaces(SWTTextEditorView.this.config.getTabs());
					
					final String updatedText = Strings.replaceTextRange(
							text,
							caretPosition,
							0,
							spaces);
					
					textWidget.setText(updatedText);
					
					textWidget.setSelection(caretPosition + spaces.length());
					
					e.doit = false;
				}
				else if (e.character == '\r') {
					
					int caretPosition = textWidget.getCaretPosition();
					
					final String text = textWidget.getText();
				
					indentOnNewline(text, caretPosition);
					
					e.doit = false;
				}
			}
		});
	}
	
	private void indentOnNewline(String text, int caretPosition) {
		int lineStart;
		
		if (caretPosition == 0) {
			lineStart = 0;
		}
		else if (text.charAt(caretPosition) == '\n') {
			lineStart = text.lastIndexOf("\n", caretPosition - 1);
		}
		else {
			lineStart = text.lastIndexOf("\n", caretPosition);
		}
		
		if (lineStart < 0) {
			lineStart = 0;
		}
		else {
			++ lineStart;
		}
		
		System.out.println("## linestart: " + lineStart + ", caretPosition " + caretPosition);
		
		final String line = text.substring(lineStart, caretPosition);
		
		
		System.out.println("### line \"" + line + "\"");
		
		final int addIndent;
		
		if (line.trim().endsWith("{")) {
			addIndent = 1;
		}
		else {
			addIndent = 0;
		}
		
		addIndentedNewline(caretPosition, line, addIndent);
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
				textWidget.getText(),
				caretPosition,
				0,
				newline);

		textWidget.setText(updatedText);
		
		textWidget.setSelection(caretPosition + newline.length());
	}

	@Override
	SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	@Override
	void setSelectedAndFocused() {
		composite.setSelection(tabItem);
		textWidget.setFocus();
	}

	@Override
	void close() {
		tabItem.dispose();
	}

	@Override
	void configure(TextEditorConfig config) {
		
		Objects.requireNonNull(config);
		
		textWidget.setTabs(config.getTabs());
		
		this.config = config;
	}

	@Override
	void setTextModel(BaseTextModel textModel) {
		// System.out.println("## setText: " + textModel.getText());
		
		this.textModel = textModel;
		
		final String text = textModel.getText();
		
		this.currentText = text;
		this.textDiffer = new TextDiffer(currentText);
		
		textWidget.setText(text);
	}
	
	private void onTextChange(String updatedText) {
		
		final ReplaceTextRange replaceTextRange = textDiffer.computeReplaceTextRange(updatedText);

		if (replaceTextRange != null) {

			System.out.println("### replace text range " + replaceTextRange);

			textModel.replaceTextRange(
					replaceTextRange.getStart(),
					replaceTextRange.getLength(),
					replaceTextRange.getText());
		}
	}
	
	@Override
	public String getText() {
		return textWidget.getText();
	}
}
