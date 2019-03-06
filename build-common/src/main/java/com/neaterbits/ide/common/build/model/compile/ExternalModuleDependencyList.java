package com.neaterbits.ide.common.build.model.compile;

import java.util.List;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class ExternalModuleDependencyList extends ModuleDependencyList {

	public ExternalModuleDependencyList(ProjectModuleResourcePath module, List<Dependency> dependencies) {
		super(module, dependencies);
	}
}
