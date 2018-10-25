package com.neaterbits.ide.common.build.model.compile;

import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ModuleResourcePath;

public final class ModuleDependencyList {

	private final ModuleResourcePath module;
	private final List<Dependency> dependencies;
	
	public ModuleDependencyList(ModuleResourcePath module, List<Dependency> dependencies) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(dependencies);
		
		this.module = module;
		this.dependencies = dependencies;
	}

	public ModuleResourcePath getModule() {
		return module;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}
}
