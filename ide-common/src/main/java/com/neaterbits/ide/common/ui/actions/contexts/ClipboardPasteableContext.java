package com.neaterbits.ide.common.ui.actions.contexts;

import java.util.Collections;
import java.util.List;

import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;

public final class ClipboardPasteableContext extends ActionContext {

	private final List<ClipboardDataType> pasteableDataTypes;

	public ClipboardPasteableContext(List<ClipboardDataType> pasteableDataTypes) {
		this.pasteableDataTypes = Collections.unmodifiableList(pasteableDataTypes);
	}

	public List<ClipboardDataType> getPasteableDataTypes() {
		return pasteableDataTypes;
	}
}
