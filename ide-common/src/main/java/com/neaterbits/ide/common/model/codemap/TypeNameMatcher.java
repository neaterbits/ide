package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
