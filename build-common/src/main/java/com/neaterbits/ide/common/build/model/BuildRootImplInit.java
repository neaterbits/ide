package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.util.modules.ModuleId;
import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.resource.LibraryResource;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ModuleResource;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResource;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

class BuildRootImplInit {

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	Map<MODULE_ID, ProjectModuleResourcePath> mapModuleIdToResourcePath(
				Map<MODULE_ID, PROJECT> projects,
				BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath = new HashMap<>(projects.size());
		
		for (PROJECT pom : projects.values()) {
			final LinkedList<ModuleResource> modules = new LinkedList<>();

			for (PROJECT project = pom; project != null; project = projects.get(buildSystemRoot.getParentModuleId(project))) {
				
				final ModuleResource moduleResource = new ModuleResource(
						buildSystemRoot.getModuleId(project),
						buildSystemRoot.getRootDirectory(project));
				
				modules.addFirst(moduleResource);
			}
			
			moduleIdToResourcePath.put(buildSystemRoot.getModuleId(pom), new ProjectModuleResourcePath(modules));
		}
		
		return moduleIdToResourcePath;
	}
	
	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	Map<ProjectModuleResourcePath, BuildProject<PROJECT>> makeBuildProjects(
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {

		final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> buildProjects = new HashMap<>();

		for (Map.Entry<MODULE_ID, PROJECT> entry : projects.entrySet()) {
			
			final MODULE_ID mavenModuleId = entry.getKey();
			
			final PROJECT pom = entry.getValue();
			
			final List<Dependency> dependencies = findDependencies(pom, projects, moduleIdToResourcePath, buildSystemRoot);
			
			final BuildProject<PROJECT> buildProject = new BuildProject<>(pom, dependencies);

			final ProjectModuleResourcePath moduleResourcePath = moduleIdToResourcePath.get(mavenModuleId);
			
			if (moduleResourcePath == null) {
				throw new IllegalStateException();
			}
			
			buildProjects.put(moduleResourcePath, buildProject);
		}
		
		return buildProjects;
	}

	private static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> List<Dependency> findDependencies(
			PROJECT project,
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final List<Dependency> dependencies;
		
		if (buildSystemRoot.getDependencies(project) != null) {
			
			dependencies = new ArrayList<>(buildSystemRoot.getDependencies(project).size());
			
			for (DEPENDENCY buildSystemDependency : buildSystemRoot.resolveDependencies(project)) {
				
				final MODULE_ID dependencyModuleId = buildSystemRoot.getDependencyModuleId(buildSystemDependency);
				
				if (dependencyModuleId == null) {
					throw new IllegalStateException();
				}
				
				final ProjectModuleResourcePath dependencyModule = moduleIdToResourcePath.get(dependencyModuleId);
				final PROJECT dependencyProject = projects.get(dependencyModuleId);
				
				final Dependency dependency;
				
				if (dependencyModule != null) {

					dependency = new BuildDependency<>(
							getCompiledModuleFile(dependencyModule, dependencyProject, buildSystemRoot),
							DependencyType.PROJECT,
							dependencyModule,
							targetDirectoryJarFile(dependencyModule, buildSystemDependency, buildSystemRoot),
							buildSystemDependency);
				}
				else {
					dependency = makeExternalDependency(buildSystemDependency, buildSystemRoot);
				}
				
				dependencies.add(dependency);
			}
		}
		else {
			dependencies = null;
		}
		
		return dependencies;
	}
	
	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> Dependency makeExternalDependency(
			DEPENDENCY buildSystemDependency,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final File repositoryJarFile = buildSystemRoot.repositoryJarFile(buildSystemDependency);

		final LibraryResourcePath resourcePath = new LibraryResourcePath(new LibraryResource(repositoryJarFile));

		return new BuildDependency<>(
				resourcePath,
				DependencyType.EXTERNAL,
				null,
				repositoryJarFile,
				buildSystemDependency);
	}
	
	private static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> File targetDirectoryJarFile(
			ProjectModuleResourcePath dependencyPath,
			DEPENDENCY mavenDependency,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		return new File(getTargetDirectory(dependencyPath, buildSystemRoot).getFile(), buildSystemRoot.compiledFileName(mavenDependency));
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> TargetDirectoryResourcePath getTargetDirectory(
			ProjectModuleResourcePath module,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final File targetDir = buildSystemRoot.getTargetDirectory(module.getFile());
		
		final TargetDirectoryResource targetDirectoryResource = new TargetDirectoryResource(targetDir); 

		return new TargetDirectoryResourcePath(module, targetDirectoryResource);
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module, PROJECT project, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {

		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(project, module.getFile());
		
		return new CompiledModuleFileResourcePath(module, new CompiledModuleFileResource(compiledModuleFile));
	}
}
