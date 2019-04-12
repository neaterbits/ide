package com.neaterbits.ide.common.ui.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.model.ISourceToken;
import com.neaterbits.compiler.util.model.IType;
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

// Delegates and allows for swapping source file model on re-parse

final class DelegatingSourceFileModel implements SourceFileModel {

	private SourceFileModel delegate;

	DelegatingSourceFileModel() {
		this.delegate = null;
	}

	DelegatingSourceFileModel(SourceFileModel delegate) {
		this.delegate = delegate;
	}
	
	void setDelegate(SourceFileModel delegate) {
		
		System.out.println("## setDelegate");
		
		Objects.requireNonNull(delegate);
		
		if (this == delegate) {
			throw new IllegalArgumentException();
		}
	
		this.delegate = delegate;
	}

	@Override
	public void iterate(SourceTokenVisitor visitor) {

		if (delegate != null) {
			delegate.iterate(visitor);
		}
	}

	@Override
	public void iterate(long offset, long length, SourceTokenVisitor visitor) {

		if (delegate != null) {
			delegate.iterate(offset, length, visitor);
		}
	}

	@Override
	public ISourceToken getSourceTokenAt(long offset) {
		return delegate != null ? delegate.getSourceTokenAt(offset) : null;
	}

	@Override
	public IType getVariableType(ISourceToken token) {
		return delegate != null ? delegate.getVariableType(token) : null;
	}

	@Override
	public List<CompileError> getParserErrors() {
		return delegate != null ? delegate.getParserErrors() : Collections.emptyList();
	}
}

