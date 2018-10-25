package com.neaterbits.ide.common.build.model;

import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

final class BuildProject<PROJECT> {

	private final PROJECT buildSystemProject;
	private final List<Dependency> dependencies;
	
	private List<SourceFolderResourcePath> sourceFolders;

	BuildProject(PROJECT buildSystemProject, List<Dependency> dependencies) {

		Objects.requireNonNull(buildSystemProject);
		
		this.buildSystemProject = buildSystemProject;
		this.dependencies = dependencies;
	}

	PROJECT getBuildSystemProject() {
		return buildSystemProject;
	}
	
	List<SourceFolderResourcePath> getSourceFolders() {
		return sourceFolders;
	}

	void setSourceFolders(List<SourceFolderResourcePath> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	List<Dependency> getDependencies() {
		return dependencies;
	}
}
