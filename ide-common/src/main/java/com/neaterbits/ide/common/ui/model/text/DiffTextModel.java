package com.neaterbits.ide.common.ui.model.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DiffTextModel extends BaseTextModel {

	private final List<TextEdit> edits;
	
	//private final String originalText;
	
	private final int originalNumLines;
	//private final long [] lineOffsets;
	
	public DiffTextModel(String lineDelimiter, String text) {
		
		super(lineDelimiter);
		
		Objects.requireNonNull(text);
		
		//this.originalText = text;
		
		final LinesFinder linesFinder = LinesFinder.findLineOffsets(text);
		
		this.originalNumLines = linesFinder.numLines;
		//this.lineOffsets = linesFinder.lines;
		
		this.edits = new ArrayList<>();
	}
	

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceTextRange(int start, int replaceLength, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTextRange(int start, int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOffsetAtLine(int lineIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLineCount() {
		
		int lineCount = originalNumLines;
		
		for (TextEdit textEdit : edits) {
			lineCount += textEdit.getChangeInNumberOfLines();
		}
		
		return lineCount;
	}
	
	@Override
	public int getLineAtOffset(int offset) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLine(int lineIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCharCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
