package com.neaterbits.ide.component.common.language.compilercommon;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.util.model.ISourceToken;
import com.neaterbits.compiler.util.model.IType;
import com.neaterbits.compiler.util.model.ProgramModel;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class CompilerSourceFileModel implements SourceFileModel {

	private final ProgramModel<?, ?, CompilationUnit> programModel;
	private final CompilationUnit sourceFile;
	private final List<CompileError> parserErrors;
	private final ResolvedTypes resolvedTypes;
	
	public CompilerSourceFileModel(ProgramModel<?, ?, CompilationUnit> programModel, CompilationUnit sourceFile, List<CompileError> parserErrors, ResolvedTypes resolvedTypes) {

		Objects.requireNonNull(programModel);
		Objects.requireNonNull(sourceFile);
		Objects.requireNonNull(resolvedTypes);

		this.programModel = programModel;
		this.sourceFile = sourceFile;
		this.parserErrors = parserErrors != null
				? Collections.unmodifiableList(parserErrors)
				: null;
				
		this.resolvedTypes = resolvedTypes;
	}

	@Override
	public void iterate(SourceTokenVisitor visitor) {
		programModel.iterate(sourceFile, visitor, resolvedTypes);
	}

	@Override
	public void iterate(long offset, long length, SourceTokenVisitor visitor) {

		iterate(token -> {
			if (token.getStartOffset() >= offset && token.getStartOffset() < offset + length) {
				visitor.onToken(token);
			}
		});
	}

	@Override
	public ISourceToken getSourceTokenAt(long offset) {

		final ISourceToken token = programModel.getTokenAt(sourceFile, offset, resolvedTypes);
		
		return token;
	}

	@Override
	public List<CompileError> getParserErrors() {
		return parserErrors;
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
