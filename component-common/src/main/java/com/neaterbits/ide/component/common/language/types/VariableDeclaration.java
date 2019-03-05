package com.neaterbits.ide.component.common.language.types;

import com.neaterbits.compiler.common.model.IType;
import com.neaterbits.compiler.common.model.VariableScope;

public interface VariableDeclaration {

	String getName();
	
	IType getType();
	
	VariableScope getScope();
}
