package com.neaterbits.ide.common.build.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.ide.common.build.model.compile.FileDependencyMap;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

final class FileDependencyMapImpl implements FileDependencyMap {

	private final Map<CompleteName, Set<SourceFileResourcePath>> filesDependingOn;
	
	FileDependencyMapImpl() {

		this.filesDependingOn = new HashMap<>();

	}
	
	@Override
	public Set<SourceFileResourcePath> getFilesDependingOn(CompleteName sourceFile) {
		return filesDependingOn.get(sourceFile);
	}

}
