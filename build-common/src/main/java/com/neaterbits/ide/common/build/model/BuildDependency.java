package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

final class BuildDependency<DEPENDENCY> extends Dependency {

	private final DEPENDENCY dependency;
	
	BuildDependency(ResourcePath resourcePath, DependencyType type, ProjectModuleResourcePath module, File compiledModuleFile, DEPENDENCY dependency) {
		super(resourcePath, type, module, compiledModuleFile);

		Objects.requireNonNull(dependency);

		this.dependency = dependency;
	}

	DEPENDENCY getDependency() {
		return dependency;
	}
}
