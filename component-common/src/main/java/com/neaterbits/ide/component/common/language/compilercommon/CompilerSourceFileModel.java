package com.neaterbits.ide.component.common.language.compilercommon;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.model.ISourceToken;
import com.neaterbits.compiler.common.model.IType;
import com.neaterbits.compiler.common.model.ProgramModel;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class CompilerSourceFileModel implements SourceFileModel {

	private final ProgramModel<?, ?, CompilationUnit> programModel;
	private final CompilationUnit sourceFile;
	
	public CompilerSourceFileModel(ProgramModel<?, ?, CompilationUnit> programModel, CompilationUnit sourceFile) {

		Objects.requireNonNull(programModel);

		this.programModel = programModel;
		
		this.sourceFile = sourceFile;
		
		if (this.sourceFile == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public ISourceToken getSourceTokenAt(long offset) {

		final ISourceToken token = programModel.getTokenAt(sourceFile, offset);
		
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
