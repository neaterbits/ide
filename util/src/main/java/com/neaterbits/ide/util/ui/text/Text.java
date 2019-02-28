package com.neaterbits.ide.util.ui.text;

import com.neaterbits.ide.util.Value;

public interface Text {

	public static final Text EMPTY_TEXT = new StringText("");
	
	public static Text replaceTextRange(Text text, long start, long replaceLength, Text toAdd) {

		final TextBuilder textBuilder = new CharText();
		
		textBuilder.append(text.substring(0, start))
			.append(toAdd)
			.append(text.substring(start + replaceLength));
		
		return textBuilder.toText();
	}

	default long findPosOfLineIndex(long lineIndexToFind, LineDelimiter lineDelimiter) {
		
		if (lineIndexToFind < 0) {
			throw new IllegalArgumentException();
		}
		
		long curPos = 0;
		long curLineIndex = 0;
		
		long foundPos = -1;
		
		while (curPos < length()) {
			
			if (curLineIndex == lineIndexToFind) {
				foundPos = curPos;
				break;
			}
			
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(curPos, lineDelimiter);
			
			if (newlineChars > 0) {
				curPos += newlineChars;
				++ curLineIndex;
			}
			else {
				++ curPos;
			}
		}

		return foundPos;
	}

	default long findLineIndexAtPos(long posToFind, LineDelimiter lineDelimiter) {

		if (posToFind < 0) {
			throw new IllegalArgumentException();
		}
		
		if (posToFind >= length()) {
			throw new IllegalArgumentException();
		}
		
		long curPos = 0;
		long lineIndex = 0;
		
		for (curPos = 0; curPos <= posToFind;) {

			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(curPos, lineDelimiter);
			
			if (newlineChars > 0) {
				curPos += newlineChars;
				
				if (curPos > posToFind) {
					break;
				}
				
				++ lineIndex;
			}
			else {
				++ curPos;
			}
		}

		return lineIndex;
	}

	default long findNewline(long startPos, LineDelimiter lineDelimiter) {

		if (startPos < 0) {
			throw new IllegalArgumentException();
		}
		
		if (startPos >= length()) {
			throw new IllegalArgumentException();
		}
		
		long i = startPos;
		long offset = -1;
		
		while (i < length()) {
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(i, lineDelimiter);
			
			if (newlineChars > 0) {
				offset = i;
				break;
			}
			else {
				++ i;
			}
		}
		
		return offset;
	}

	default long getNumberOfLines(LineDelimiter lineDelimiter) {
		
		final Value<Long> value = new Value<>();
		
		final long numNewlines = getNumberOfNewlineChars(lineDelimiter, value);
	
		final long numLines;
		
		if (value.get() != null && value.get() < length()) {
			numLines = numNewlines + 1;
		}
		else {
			numLines = numNewlines;
		}
		
		return numLines;
	}
	
	default long getNumberOfNewlineChars(LineDelimiter lineDelimiter) {
		return getNumberOfNewlineChars(lineDelimiter, null);
	}
	
	default long getNumberOfNewlineChars(LineDelimiter lineDelimiter, Value<Long> offsetAfterLastNewline) {

		long i = 0;

		long numLines = 0;
		
		long offsetAfterLast = -1;
		
		for (; i < length();) {
			
			final long newlineChars = getNumberOfNewlineCharsForOneLineShift(i, lineDelimiter);
			
			if (newlineChars > 0) {
				
				i += newlineChars;
				
				offsetAfterLast = i;
				
				++ numLines;
			}
			else {
				++ i;
			}
		}
		
		if (offsetAfterLast != -1 && offsetAfterLastNewline != null) {
			offsetAfterLastNewline.set(offsetAfterLast);
		}

		return numLines;
	}

	default int getNumberOfNewlineCharsForOneLineShift(long offset, LineDelimiter lineDelimiter) {
		return lineDelimiter.getNumberOfNewlineCharsForOneLineShift(this, offset);
	}
	

	boolean isEmpty();
	
	long length();
	
	char charAt(long index);
	
	Text substring(long beginIndex);

	Text substring(long beginIndex, long endIndex);

	Text merge(Text other);
	
	default long findBeginningOfNextLine(long offset, LineDelimiter lineDelimiter) {
		
		if (offset < 0) {
			throw new IllegalArgumentException();
		}

		if (offset >= length()) {
			throw new IllegalArgumentException();
		}

		final long newlineOffset = findNewline(offset, lineDelimiter);
		
		final long result;
		
		if (newlineOffset == -1) {
			result = -1;
		}
		else {
			
			final long newlineChars = lineDelimiter.getNumberOfNewlineCharsForOneLineShift(this, newlineOffset);
			
			if (newlineOffset + newlineChars >= length()) {
				result = -1;
			}
			else {
				result = newlineOffset + newlineChars;
			}
		}
		
		return result;
	}
	
	String asString();
}
