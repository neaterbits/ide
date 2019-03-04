package com.neaterbits.ide.common.ui.actions.contexts;

import com.neaterbits.ide.common.resource.SourceLineResourcePath;

public class SearchResultContext extends ActionContext {

	private final SourceLineResourcePath sourceLine;

	public SearchResultContext(SourceLineResourcePath sourceLine) {
		this.sourceLine = sourceLine;
	}

	public SourceLineResourcePath getSourceLine() {
		return sourceLine;
	}
}
