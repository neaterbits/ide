package com.neaterbits.ide.component.common.language.model;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.util.model.ISourceToken;
import com.neaterbits.compiler.util.model.IType;
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.parse.CompileError;

public interface SourceFileModel {

	void iterate(SourceTokenVisitor visitor);

	ISourceToken getSourceTokenAt(long offset);

	IType getVariableType(ISourceToken token);

	List<CompileError> getParserErrors();
	
	public static ISourceTokenProperties getProperties(ISourceToken token) {

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
			
		case CALL_PARAMETER:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case NAMESPACE_DECLARATION_NAME:
			break;

		case IMPORT_NAME:
			break;
			
		case UNKNOWN:
			break;

		}

		return new SourceTokenProperties(new SourceElementMask(flags));
	}
	
}
