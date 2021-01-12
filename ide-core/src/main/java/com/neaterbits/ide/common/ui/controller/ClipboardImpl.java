package com.neaterbits.ide.common.ui.controller;

import java.util.Objects;

import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.clipboard.ClipboardData;
import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.ui.view.SystemClipboard;

final class ClipboardImpl implements Clipboard {

	private final SystemClipboard systemClipboard;
	
	public ClipboardImpl(SystemClipboard systemClipboard) {

		Objects.requireNonNull(systemClipboard);
		
		this.systemClipboard = systemClipboard;
	}

	private ClipboardData data;
	
	@Override
	public ClipboardData getData() {
		return data != null ? data : systemClipboard.getData();
	}

	@Override
	public ClipboardDataType getDataType() {
		return data != null ? data.getDataType() : systemClipboard.getDataType();
	}

	@Override
	public boolean hasDataType(ClipboardDataType dataType) {
		
		Objects.requireNonNull(dataType);
		
		final boolean hasDataType = data != null ? dataType.equals(data.getDataType()) : false;
		
		return hasDataType ? true : systemClipboard.hasDataType(dataType);
	}
}
