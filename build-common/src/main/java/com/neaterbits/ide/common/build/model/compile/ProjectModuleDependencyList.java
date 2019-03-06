package com.neaterbits.ide.common.build.model.compile;

import java.util.List;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class ProjectModuleDependencyList extends ModuleDependencyList {

	public ProjectModuleDependencyList(ProjectModuleResourcePath module, List<Dependency> dependencies) {
		super(module, dependencies);
	}
}
