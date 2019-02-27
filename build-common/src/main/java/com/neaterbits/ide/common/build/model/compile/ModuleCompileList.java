package com.neaterbits.ide.common.build.model.compile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ModuleResourcePath;

public final class ModuleCompileList {

	private final ModuleResourcePath module;
	private final List<SourceFolderCompileList> sourceFiles;

	public ModuleCompileList(ModuleResourcePath module, Collection<SourceFolderCompileList> sourceFiles) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(sourceFiles);
		
		this.module = module;
		this.sourceFiles = Collections.unmodifiableList(new ArrayList<>(sourceFiles));
	}
	
	public ModuleResourcePath getModule() {
		return module;
	}

	public List<SourceFolderCompileList> getSourceFiles() {
		return sourceFiles;
	}

	@Override
	public String toString() {
		return module.getName() + "/" + sourceFiles;
	}
}
