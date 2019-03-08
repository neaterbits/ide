package com.neaterbits.ide.component.common.language.compilercommon;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.model.ISourceToken;
import com.neaterbits.compiler.common.model.IType;
import com.neaterbits.compiler.common.model.ProgramModel;
import com.neaterbits.compiler.common.model.ResolvedTypes;
import com.neaterbits.compiler.common.model.SourceTokenVisitor;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class CompilerSourceFileModel implements SourceFileModel {

	private final ProgramModel<?, ?, CompilationUnit> programModel;
	private final CompilationUnit sourceFile;
	private final ResolvedTypes resolvedTypes;
	
	public CompilerSourceFileModel(ProgramModel<?, ?, CompilationUnit> programModel, CompilationUnit sourceFile, ResolvedTypes resolvedTypes) {

		Objects.requireNonNull(programModel);
		Objects.requireNonNull(sourceFile);
		Objects.requireNonNull(resolvedTypes);

		this.programModel = programModel;
		this.sourceFile = sourceFile;
		this.resolvedTypes = resolvedTypes;
	}

	@Override
	public void iterate(SourceTokenVisitor visitor) {
		programModel.iterate(sourceFile, visitor, resolvedTypes);
	}

	@Override
	public ISourceToken getSourceTokenAt(long offset) {

		final ISourceToken token = programModel.getTokenAt(sourceFile, offset, resolvedTypes);
		
		return token;
	}

	@Override
	public IType getVariableType(ISourceToken token) {
		
		if (token.getTokenType().isVariable()) {
			throw new IllegalArgumentException();
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
