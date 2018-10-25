package com.neaterbits.ide.common.build.model;

import java.util.Set;

import com.neaterbits.ide.common.resource.ModuleResourcePath;

public interface DependencyMap {

	Set<ModuleResourcePath> findLeafModules();
	
	Set<ModuleResourcePath> getDependencies(ModuleResourcePath resourcePath);
	
}
