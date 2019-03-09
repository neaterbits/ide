package com.neaterbits.ide.common.build.model.compile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.scheduling.dependencies.CollectedObject;

public class ModuleDependencyList implements CollectedObject {

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

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [module=" + module + ", dependencies=" + dependencies + "]";
	}

	@Override
	public final String getName() {
		return module.getName();
	}

	@Override
	public final List<String> getCollected() {
		return dependencies.stream()
				.map(dependency -> dependency.getCompiledModuleFile().getName())
				.collect(Collectors.toList());
	}
}
