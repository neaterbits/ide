package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public abstract class Dependency {

	private final ResourcePath resourcePath;
	private final DependencyType type;
	private final ModuleResourcePath module;
	private final File compiledModuleFile;

	public Dependency(ResourcePath resourcePath, DependencyType type, ModuleResourcePath module, File compiledModuleFile) {
		
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

	public final ModuleResourcePath getModule() {
		return module;
	}

	public final File getCompiledModuleFile() {
		return compiledModuleFile;
	}

	@Override
	public String toString() {
		return module != null ? module.getName() : compiledModuleFile.getName();
	}
}
