package com.neaterbits.ide.component.common.language.model;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.model.ISourceToken;
import com.neaterbits.compiler.common.model.IType;

public interface SourceFileModel {

	ISourceToken getSourceTokenAt(long offset);

	IType getVariableType(ISourceToken token);

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
			
		case PACKAGE_DECLARATION_NAME:
			break;

		case UNKNOWN:
			break;

		}

		return new SourceTokenProperties(new SourceElementMask(flags));
	}
	
}
