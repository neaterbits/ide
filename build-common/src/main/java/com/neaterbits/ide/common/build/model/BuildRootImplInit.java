package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.resource.LibraryResource;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ModuleResource;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResource;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

class BuildRootImplInit {

	static <MODULE_ID, PROJECT, DEPENDENCY>
	Map<MODULE_ID, ModuleResourcePath> mapModuleIdToResourcePath(Map<MODULE_ID, PROJECT> projects, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final Map<MODULE_ID, ModuleResourcePath> moduleIdToResourcePath = new HashMap<>(projects.size());
		
		for (PROJECT pom : projects.values()) {
			final LinkedList<ModuleResource> modules = new LinkedList<>();

			for (PROJECT project = pom; project != null; project = projects.get(buildSystemRoot.getParentModuleId(project))) {
				
				final ModuleResource moduleResource = new ModuleResource(
						buildSystemRoot.getRootDirectory(project),
						buildSystemRoot.getNonScopedName(project));
				
				modules.addFirst(moduleResource);
			}
			
			moduleIdToResourcePath.put(buildSystemRoot.getModuleId(pom), new ModuleResourcePath(modules));
		}
		
		return moduleIdToResourcePath;
	}
	
	static <MODULE_ID, PROJECT, DEPENDENCY>
	Map<ModuleResourcePath, BuildProject<PROJECT>> makeBuildProjects(
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {

		final Map<ModuleResourcePath, BuildProject<PROJECT>> buildProjects = new HashMap<>();

		for (Map.Entry<MODULE_ID, PROJECT> entry : projects.entrySet()) {
			
			final MODULE_ID mavenModuleId = entry.getKey();
			
			final PROJECT pom = entry.getValue();
			
			final BuildProject<PROJECT> buildProject = new BuildProject<>(pom, findDependencies(pom, moduleIdToResourcePath, buildSystemRoot));

			final ModuleResourcePath moduleResourcePath = moduleIdToResourcePath.get(mavenModuleId);
			
			if (moduleResourcePath == null) {
				throw new IllegalStateException();
			}
			
			buildProjects.put(moduleResourcePath, buildProject);
		}
		
		return buildProjects;
	}

	private static <MODULE_ID, PROJECT, DEPENDENCY> List<Dependency> findDependencies(
			PROJECT project,
			Map<MODULE_ID, ModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final List<Dependency> dependencies;
		
		if (buildSystemRoot.getDependencies(project) != null) {
			
			dependencies = new ArrayList<>(buildSystemRoot.getDependencies(project).size());
			
			for (DEPENDENCY buildSystemDependency : buildSystemRoot.resolveDependencies(project)) {
				
				final MODULE_ID moduleId = buildSystemRoot.getDependencyModuleId(buildSystemDependency);
				
				if (moduleId == null) {
					throw new IllegalStateException();
				}
				
				final ModuleResourcePath projectModule = moduleIdToResourcePath.get(moduleId);
				
				final Dependency dependency;
				
				if (projectModule != null) {

					dependency = new BuildDependency<>(
							getCompiledModuleFile(projectModule, project, buildSystemRoot),
							DependencyType.PROJECT,
							projectModule,
							targetDirectoryJarFile(projectModule, buildSystemDependency, buildSystemRoot),
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
	
	static <MODULE_ID, PROJECT, DEPENDENCY> Dependency makeExternalDependency(DEPENDENCY buildSystemDependency, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final File repositoryJarFile = buildSystemRoot.repositoryJarFile(buildSystemDependency);

		final LibraryResourcePath resourcePath = new LibraryResourcePath(new LibraryResource(repositoryJarFile));

		return new BuildDependency<>(
				resourcePath,
				DependencyType.EXTERNAL,
				null,
				repositoryJarFile,
				buildSystemDependency);
	}
	
	private static <MODULE_ID, PROJECT, DEPENDENCY> File targetDirectoryJarFile(
			ModuleResourcePath dependencyPath,
			DEPENDENCY mavenDependency,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		return new File(getTargetDirectory(dependencyPath, buildSystemRoot).getFile(), buildSystemRoot.compiledFileName(mavenDependency));
	}

	static <MODULE_ID, PROJECT, DEPENDENCY> TargetDirectoryResourcePath getTargetDirectory(ModuleResourcePath module, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		final File targetDir = buildSystemRoot.getTargetDirectory(module.getFile());
		
		final TargetDirectoryResource targetDirectoryResource = new TargetDirectoryResource(targetDir); 

		return new TargetDirectoryResourcePath(module, targetDirectoryResource);
	}

	static <MODULE_ID, PROJECT, DEPENDENCY>
	CompiledModuleFileResourcePath getCompiledModuleFile(ModuleResourcePath module, PROJECT project, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {

		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(project, module.getFile());
		
		return new CompiledModuleFileResourcePath(module, new CompiledModuleFileResource(compiledModuleFile));
	}
}
