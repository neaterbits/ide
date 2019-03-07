package com.neaterbits.ide.common.build.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.DependencyType;
import com.neaterbits.ide.common.buildsystem.Scope;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public class ModuleBuilderUtil {

	public static List<Dependency> transitiveProjectDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {
		
		final List<Dependency> dependencies = new ArrayList<>();
		
		transitiveProjectDependencies(context, module, dependencies);

		return dependencies;
	}

	private static void transitiveProjectDependencies(TaskBuilderContext context, ProjectModuleResourcePath module, List<Dependency> dependencies) {
		 
		final List<Dependency> moduleDependencies = context.getBuildRoot().getDependenciesForProjectModule(module);
		 
		dependencies.addAll(moduleDependencies);

		for (Dependency dependency : moduleDependencies) {
			if (dependency.getType() == DependencyType.PROJECT) {
				transitiveProjectDependencies(context, dependency.getModule(), dependencies);
			}
		}
	}

	public static List<Dependency> transitiveExternalDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {

		final List<Dependency> moduleDependencies = transitiveProjectDependencies(context, module);
		
		final List<Dependency> moduleExternalDependencies = moduleDependencies.stream()
				.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
				.collect(Collectors.toList());
		
		final List<Dependency> dependencies = new ArrayList<>();
		
		dependencies.addAll(moduleExternalDependencies);
		
		for (Dependency externalDependency : moduleExternalDependencies) {
			transitiveDependencies(context, externalDependency, dependencies);
		}

		return dependencies;
	}

	static List<Dependency> transitiveDependencies(TaskBuilderContext context, Dependency module) {
		
		final List<Dependency> dependencies = new ArrayList<>();
		
		transitiveDependencies(context, module, dependencies);

		return dependencies;
	}

	private static void transitiveDependencies(TaskBuilderContext context, Dependency dependency, List<Dependency> dependencies) {

		final List<Dependency> moduleDependencies;
		
		switch (dependency.getType()) {
		case PROJECT:
			moduleDependencies = context.getBuildRoot().getDependenciesForProjectModule((ProjectModuleResourcePath)dependency.getResourcePath());
			break;
			
		case EXTERNAL:
			moduleDependencies = context.getBuildRoot().getDependenciesForExternalLibrary(dependency, Scope.COMPILE, false);
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		 
		dependencies.addAll(moduleDependencies);

		for (Dependency foundDep : moduleDependencies) {
			try {
				transitiveDependencies(context, foundDep, dependencies);
			}
			catch (Exception ex) {
				throw new IllegalStateException("Exception while getting dependencies for " + dependency, ex);
			}
		}
	}
}
