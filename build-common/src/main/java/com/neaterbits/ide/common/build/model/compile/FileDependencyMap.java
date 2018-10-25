package com.neaterbits.ide.common.build.model.compile;

import java.util.Set;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface FileDependencyMap {

	Set<SourceFileResourcePath> getFilesDependingOn(CompleteName completeName);
	
}
