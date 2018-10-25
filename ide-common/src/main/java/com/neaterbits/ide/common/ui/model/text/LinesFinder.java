package com.neaterbits.ide.common.ui.model.text;

import java.util.Arrays;

final class LinesFinder {
	
	int numLines = 0;
	long [] lines = new long[10000];
	
	private static long line(long pos, long length) {
		return pos << 32 | length;
	}

	public long getStartOfCurLine() {
		final long lastLine = lines[numLines - 1];
		
		return lastLine >>> 32 + lastLine & 0xFFFFFFFL;
	}
	
	void addLine(int startOfNextLine) {

		if (numLines == 0) {
			lines[0] = line(0, startOfNextLine);
		}
		else {
			
			if (numLines == lines.length) {
				lines = Arrays.copyOf(lines, lines.length * 4);
			}

			final long startOfLine = getStartOfCurLine();

			lines[numLines ++] = line(startOfLine, startOfNextLine - startOfLine);
		}
	}

	static int findPosOfLineNo(String text, int lineNo) {
		
		int i = 0;
		
		int line = 0;
		
		for (; i < text.length() && line < lineNo; ++ i) {
			final int newlineChars = getNumberOfNewlineChars(text, i);
			
			if (newlineChars > 0) {
				i += newlineChars;
				++ line;
			}
			else {
				++ i;
			}
		}

		return i;
	}

	static int findLineNoAtPos(String text, int pos) {

		int i = 0;
		
		int lineNo = 0;
		
		for (; i <= pos; ++ i) {
			final int newlineChars = getNumberOfNewlineChars(text, i);
			
			if (newlineChars > 0) {
				i += newlineChars;
				++ lineNo;
			}
			else {
				++ i;
			}
		}

		return lineNo;
	}
	
	static int findNewline(String text, int startPos) {

		int i = startPos;
		
		for (; i < text.length(); ++ i) {
			final int newlineChars = getNumberOfNewlineChars(text, i);
			
			if (newlineChars > 0) {
				break;
			}
			else {
				++ i;
			}
		}
		
		return i;
	}
	
	static int getNumberOfNewlineChars(String text) {

		int i = 0;

		for (; i < text.length();) {
			
			final int newlineChars = getNumberOfNewlineChars(text, i);
			
			if (newlineChars > 0) {
				i += newlineChars;
			}
			else {
				++ i;
			}
		}

		return i;
	}

	private static int getNumberOfNewlineChars(String text, int i) {

		final char c = text.charAt(i);
		
		int newlineChars = 0;
		
		if (c == '\r') {
			newlineChars = 1;
			
			if (i < text.length() - 1 && text.charAt(i + 1) == '\n') {
				++ newlineChars;
			}
		}
		else if (c == '\n') {
			newlineChars = 1;
		}

		return newlineChars;
	}
	
	static LinesFinder findLineOffsets(String text) {
		
		final LinesFinder linesFinder = new LinesFinder();

		for (int i = 0; i < text.length();) {
			
			final int newlineChars = getNumberOfNewlineChars(text, i);
			
			if (newlineChars > 0) {
				linesFinder.addLine(i + newlineChars);
			}
			else {
				++ i;
			}
		}

		return linesFinder;
	}
}
