package com.neaterbits.ide.common.build.model.compile;

import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class ModuleDependencyList {

	private final ProjectModuleResourcePath module;
	private final List<Dependency> dependencies;
	
	public ModuleDependencyList(ProjectModuleResourcePath module, List<Dependency> dependencies) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(dependencies);
		
		this.module = module;
		this.dependencies = dependencies;
	}

	public ProjectModuleResourcePath getModule() {
		return module;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}
}
