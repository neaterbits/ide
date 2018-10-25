package com.neaterbits.ide.swt;

import com.neaterbits.ide.common.ui.model.text.BaseTextModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorView;

abstract class SWTEditorView implements EditorView {

	
	SWTEditorView() {
		
		// final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		
		// tabItem.setText(fileName);

		/*
		final Label label = new Label(composite, SWT.NONE);
		
		label.setText("Text123");
		*/
		
		
		// this.textWidget = new StyledText(composite, SWT.NONE);
		
//		textWidget.setEditable(true);
		
		
		// tabItem.setControl(textWidget);

	}

	abstract void setFocused();

	abstract void close();
	
	abstract void configure(TextEditorConfig config);

	void setTextModel(BaseTextModel textModel) {
		/*
		textWidget.setContent(new StyledTextContent() {
			
			@Override
			public void setText(String text) {
				textModel.getText();
			}
			
			@Override
			public void replaceTextRange(int start, int replaceLength, String text) {
				textModel.replaceTextRange(start, replaceLength, text);
			}
			
			@Override
			public void removeTextChangeListener(TextChangeListener listener) {
				
			}
			
			@Override
			public String getTextRange(int start, int length) {
				return textModel.getTextRange(start, length);
			}
			
			@Override
			public int getOffsetAtLine(int lineIndex) {
				return textModel.getOffsetAtLine(lineIndex);
			}
			
			@Override
			public String getLineDelimiter() {
				return textModel.getLineDelimiter();
			}
			
			@Override
			public int getLineCount() {
				return textModel.getLineCount();
			}
			
			@Override
			public int getLineAtOffset(int offset) {
				return textModel.getLineAtOffset(offset);
			}
			
			@Override
			public String getLine(int lineIndex) {
				return textModel.getLine(lineIndex);
			}
			
			@Override
			public int getCharCount() {
				return textModel.getCharCount();
			}
			
			@Override
			public void addTextChangeListener(TextChangeListener listener) {
				
			}
		});
		*/
	}
}
