package com.neaterbits.ide.common.project;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ModuleId;

public class Project {

	private final List<SourceFolder> sourceFolders;
	
	private final List<ModuleId> dependencies;

	public Project(List<SourceFolder> sourceFolders, List<ModuleId> dependencies) {
		
		Objects.requireNonNull(sourceFolders);
		Objects.requireNonNull(dependencies);
		
		this.sourceFolders = sourceFolders;
		this.dependencies = dependencies;
	}

	public List<SourceFolder> getSourceFolders() {
		return sourceFolders;
	}

	public List<ModuleId> getDependencies() {
		return dependencies;
	}
}

