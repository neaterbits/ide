package com.neaterbits.ide.common.ui.model.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.model.ProgramModel;
import com.neaterbits.compiler.common.model.SourceToken;

final class CompilerSourceFileModel<SOURCEFILE> implements SourceFileModel {

	private final ProgramModel<?, SOURCEFILE> programModel;
	private final SOURCEFILE sourceFile;
	
	CompilerSourceFileModel(ProgramModel<?, SOURCEFILE> programModel, File sourceFile) {

		Objects.requireNonNull(programModel);

		this.programModel = programModel;
		
		this.sourceFile = programModel.getSourceFile(sourceFile);
		
		if (this.sourceFile == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public SourceElement getSourceTokenAt(long offset) {

		final SourceToken token = programModel.getTokenAt(sourceFile, offset);
		
		final SourceElement sourceElement;

		final List<SourceElementFlag> flags = new ArrayList<>();

		switch (token.getTokenType()) {
		
		case KEYWORD:
			break;
			
		case CLASS_DECLARATION_NAME:
		case INTERFACE_DECLARATION_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			flags.add(SourceElementFlag.MOVEABLE);
			break;
			
		case METHOD_DECLARATION_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case CLASS_REFERENCE_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case METHOD_REFERENCE_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case INSTANCE_VARIABLE:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case LOCAL_VARIABLE:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case CALL_PARAMETER_USE:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			

		case UNKNOWN:
			break;

		}

		sourceElement = new SourceElementImpl(
				token.getStartOffset(),
				token.getLength(),
				new SourceElementMask(flags));
		
		return sourceElement;
	}
}
