package com.neaterbits.ide.common.ui.actions.contexts;

import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public final class EditorContext extends ActionContext {

	private final SourceFileResourcePath sourceFile;

	public EditorContext(SourceFileResourcePath sourceFile) {
	
		Objects.requireNonNull(sourceFile);
		
		this.sourceFile = sourceFile;
	}

	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}
}
