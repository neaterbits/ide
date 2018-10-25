package com.neaterbits.ide.common.ui.model.text;

import com.neaterbits.compiler.common.util.Strings;

public final class StringTextModel extends BaseTextModel {

	private String text;

	public StringTextModel(String lineDelimiter, String text) {
		super(lineDelimiter);

		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void replaceTextRange(int start, int replaceLength, String text) {

		this.text = Strings.replaceTextRange(this.text, start, replaceLength, text);
		
		/*
		System.out.println("## textmodel replace text range: \"" + text + "\"");
		System.out.println("## textModel updated text " + this.text);
		*/
	}

	@Override
	public String getTextRange(int start, int length) {
		return text.substring(start, start + length);
	}

	@Override
	public int getOffsetAtLine(int lineIndex) {
		return LinesFinder.findPosOfLineNo(text, lineIndex);
	}

	@Override
	public int getLineCount() {
		return LinesFinder.getNumberOfNewlineChars(text);
	}

	@Override
	public int getLineAtOffset(int offset) {
		return LinesFinder.findLineNoAtPos(text, offset);
	}

	@Override
	public String getLine(int lineIndex) {
		
		final int pos = LinesFinder.findPosOfLineNo(text, lineIndex);
		
		final int eol = LinesFinder.findNewline(text, pos);
		
		return text.substring(pos, eol);
	}

	@Override
	public int getCharCount() {
		return text.length();
	}
}
