package com.neaterbits.ide.common.build.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.build.model.ProjectDependency;
import com.neaterbits.ide.common.buildsystem.Scope;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public class ModuleBuilderUtil {
	
	public static List<ProjectDependency> transitiveProjectDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {

		return transitiveProjectDependencies(context.getBuildRoot(), module);
	}
		
	public static List<ProjectDependency> transitiveProjectDependencies(BuildRoot buildRoot, ProjectModuleResourcePath module) {
		
		final List<ProjectDependency> dependencies = new ArrayList<>();
		
		transitiveProjectDependencies(buildRoot, module, dependencies);

		return dependencies;
	}

	private static void transitiveProjectDependencies(BuildRoot buildRoot, ProjectModuleResourcePath module, List<ProjectDependency> dependencies) {
		 
		final List<ProjectDependency> moduleDependencies = buildRoot.getProjectDependenciesForProjectModule(module);
		 
		dependencies.addAll(moduleDependencies);

		for (ProjectDependency dependency : moduleDependencies) {
			transitiveProjectDependencies(buildRoot, dependency.getModulePath(), dependencies);
		}
	}

	public static List<LibraryDependency> transitiveProjectExternalDependencies(TaskBuilderContext context, ProjectModuleResourcePath module) {

		final List<ProjectDependency> moduleDependencies = transitiveProjectDependencies(context, module);
		
		final List<LibraryDependency> moduleExternalDependencies = moduleDependencies.stream()
				.flatMap(projectDependency -> context.getBuildRoot().getLibraryDependenciesForProjectModule(projectDependency.getModulePath()).stream())
				.collect(Collectors.toList());
		
		final List<LibraryDependency> dependencies = new ArrayList<>();
		
		dependencies.addAll(moduleExternalDependencies);
		
		for (LibraryDependency externalDependency : moduleExternalDependencies) {
			transitiveDependencies(context, externalDependency, dependencies);
		}

		return dependencies;
	}

	static List<LibraryDependency> transitiveLibraryDependencies(TaskBuilderContext context, LibraryDependency module) {
		
		final List<LibraryDependency> dependencies = new ArrayList<>();
		
		transitiveDependencies(context, module, dependencies);

		return dependencies;
	}

	private static void transitiveDependencies(TaskBuilderContext context, LibraryDependency dependency, List<LibraryDependency> dependencies) {

		final List<LibraryDependency> moduleDependencies;

		moduleDependencies = context.getBuildRoot().getDependenciesForExternalLibrary(dependency, Scope.COMPILE, false);
		 
		dependencies.addAll(moduleDependencies);

		for (LibraryDependency foundDep : moduleDependencies) {
			try {
				transitiveDependencies(context, foundDep, dependencies);
			}
			catch (Exception ex) {
				throw new IllegalStateException("Exception while getting dependencies for " + dependency, ex);
			}
		}
	}
}
