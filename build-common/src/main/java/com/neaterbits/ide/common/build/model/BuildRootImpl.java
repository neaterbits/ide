package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.buildsystem.BuildSystemRootScan;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

public class BuildRootImpl<MODULE_ID, PROJECT, DEPENDENCY> implements BuildRoot {

	private final File path;
	private final BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot;
	
	private final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> projects;
	private final Map<MODULE_ID, PROJECT> buildSystemProjectByModuleId;
	private final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath;

	private final List<BuildRootListener> listeners;
	
	public BuildRootImpl(File path, BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> buildSystemRoot) {
		
		Objects.requireNonNull(path);
		
		this.path = path;
		
		this.buildSystemRoot = buildSystemRoot;
		this.listeners = new ArrayList<>();
		
		final Collection<PROJECT> projects = buildSystemRoot.getProjects();
		
		this.buildSystemProjectByModuleId = projects.stream()
				.collect(Collectors.toMap(buildSystemRoot::getModuleId, project -> project));
		
		this.moduleIdToResourcePath = BuildRootImplInit.mapModuleIdToResourcePath(buildSystemProjectByModuleId, buildSystemRoot);
		
		this.projects = BuildRootImplInit.makeBuildProjects(buildSystemProjectByModuleId, moduleIdToResourcePath, buildSystemRoot);
	}
	
	@Override
	public File getPath() {
		return path;
	}

	@Override
	public Collection<ProjectModuleResourcePath> getModules() {
		return Collections.unmodifiableCollection(moduleIdToResourcePath.values());
	}
	
	@Override
	public void setSourceFolders(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders) {

		System.out.println("## set sourcefolders for " + module);

		projects.get(module).setSourceFolders(sourceFolders);
		
		for (BuildRootListener buildRootListener : listeners) {
			buildRootListener.onSourceFoldersChanged(module);
		}
	}
	
	@Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {
		
		final BuildProject<PROJECT> buildProject = projects.get(module);
		
		if (buildProject == null) {
			throw new IllegalArgumentException("No buildproject with name " + module);
		}
		
		return buildProject.getSourceFolders();
	}

	@Override
	public List<Dependency> getDependenciesForProjectModule(ProjectModuleResourcePath module) {
		return projects.get(module).getDependencies();
	}
	
	@Override
	public List<Dependency> getDependenciesForExternalLibrary(Dependency dependency) {

		try {
			return getTransitiveExternalDependencies(dependency);
		}
		catch (ScanException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module) {
		return BuildRootImplInit.getTargetDirectory(module, buildSystemRoot);
	}

	@Override
	public void downloadExternalDependencyAndAddToBuildModel(Dependency dependency) {
		
		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)dependency;

		buildSystemRoot.downloadExternalDependencyIfNotPresent(buildDependency.getDependency());
	}

	private List<Dependency> getTransitiveExternalDependencies(Dependency dependency) throws ScanException {
		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)dependency;

		return buildSystemRoot.getTransitiveExternalDependencies(buildDependency.getDependency()).stream()
				.map(transitive -> BuildRootImplInit.makeExternalDependency(transitive, buildSystemRoot))
				.collect(Collectors.toList());
	}

	@Override
	public CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {

		return BuildRootImplInit.getCompiledModuleFile(module, projects.get(module).getBuildSystemProject(), buildSystemRoot);
	}

	@Override
	public void addListener(BuildRootListener listener) {
		Objects.requireNonNull(listener);

		listeners.add(listener);
	}

	@Override
	public BuildSystemRootScan getBuildSystemRootScan() {
		return buildSystemRoot;
	}
}
