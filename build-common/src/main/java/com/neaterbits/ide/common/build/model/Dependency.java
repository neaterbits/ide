package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public abstract class Dependency {

	private final ResourcePath resourcePath;
	private final ProjectModuleResourcePath module;
	private final DependencyType type;
	private final File compiledModuleFile;

	public Dependency(ResourcePath resourcePath, DependencyType type, ProjectModuleResourcePath module, File compiledModuleFile) {
		
		Objects.requireNonNull(resourcePath);
		Objects.requireNonNull(type);
		Objects.requireNonNull(compiledModuleFile);
		
		this.resourcePath = resourcePath;
		this.type = type;
		
		if (type == DependencyType.PROJECT) {
			Objects.requireNonNull(module);
		}
		
		this.module = module;
		
		this.compiledModuleFile = compiledModuleFile;
	}

	public final ResourcePath getResourcePath() {
		return resourcePath;
	}

	public final DependencyType getType() {
		return type;
	}

	public final ProjectModuleResourcePath getModule() {
		return module;
	}

	public final File getCompiledModuleFile() {
		return compiledModuleFile;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependency other = (Dependency) obj;
		if (resourcePath == null) {
			if (other.resourcePath != null)
				return false;
		} else if (!resourcePath.equals(other.resourcePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return module != null ? module.getName() : compiledModuleFile.getName();
	}
}
