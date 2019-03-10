package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
