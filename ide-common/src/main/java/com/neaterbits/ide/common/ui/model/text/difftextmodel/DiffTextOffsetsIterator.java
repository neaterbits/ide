package com.neaterbits.ide.common.ui.model.text.difftextmodel;

interface DiffTextOffsetsIterator<T> {

	boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, T state);
	
	boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, T state);

}
