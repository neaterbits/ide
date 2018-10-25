package com.neaterbits.ide.common.ui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import com.neaterbits.compiler.common.util.Files;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootListener;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner.Namespace;
import com.neaterbits.ide.common.resource.ModuleResource;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResource;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public class ProjectModel {

	private final BuildRoot buildRoot;
	
	private final List<ProjectModelListener> modelListeners;

	public ProjectModel(BuildRoot buildRoot) {

		Objects.requireNonNull(buildRoot);

		this.buildRoot = buildRoot;
		
		this.modelListeners = new ArrayList<>();
		
		buildRoot.addListener(new BuildRootListener() {
			
			@Override
			public void onSourceFoldersChanged(ModuleResourcePath module) {
				for (ProjectModelListener projectModelListener : modelListeners) {
					projectModelListener.onModelChanged();
				}
			}
		});
	}
	
	public void addListener(ProjectModelListener listener) {
		Objects.requireNonNull(listener);

		modelListeners.add(listener);
	}
	
	public ModuleResourcePath getRoot() {
		return buildRoot.getModules().stream()
		.filter(module -> module.isAtRoot())
		.map(module -> new ModuleResourcePath(new ModuleResource(module.getFile(), ((ModuleResource)module.getLast()).getName())))
		.findFirst()
		.get();
	}
	
	public List<ResourcePath> getResources(ResourcePath path) {
		
		Objects.requireNonNull(path);

		final List<ResourcePath> resources = new ArrayList<ResourcePath>();

		if (path.isAtRoot()) {
			findSubModulesAndFolders((ModuleResourcePath)path, resources);
		}
		else if (path instanceof ModuleResourcePath) {
			findSubModulesAndFolders((ModuleResourcePath)path, resources);
		}
		else if (path instanceof SourceFolderResourcePath) {

			final SourceFolderResourcePath sourceFolderResourcePath = (SourceFolderResourcePath)path;

			final SourceFolderResource sourceFolderResource = sourceFolderResourcePath.getSourceFolder();

			if (sourceFolderResource.getLanguage() != null && sourceFolderResource.getLanguage().hasFolderNamespaces()) {
				findNamespaces(sourceFolderResourcePath, resources);
			}
			else {
				SourceFileScanner.findSourceFiles(sourceFolderResourcePath, sourceFolderResource.getFile(), resources);
			}
		}
		else if (path instanceof NamespaceResourcePath) {
			final NamespaceResourcePath namespaceResourcePath = (NamespaceResourcePath)path;
		
			SourceFileScanner.findSourceFiles(namespaceResourcePath, namespaceResourcePath.getFile(), resources);
		}
		else if (path instanceof SourceFileResourcePath) {
		
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return resources;
	}

	
	private void findSubModulesAndFolders(ModuleResourcePath modulePath, List<ResourcePath> resources) {
		
		final Collection<ModuleResourcePath> modules = buildRoot.getModules();
		
		for (ModuleResourcePath module : modules) {
			
			if (module.isDirectSubModuleOf(modulePath)) {
				resources.add(module);
			}
		}
		
		final Collection<SourceFolderResourcePath> sourceFolders = buildRoot.getSourceFolders(modulePath);
	
		if (sourceFolders != null) {
			resources.addAll(sourceFolders);
		}
	}

	private void findNamespaces(SourceFolderResourcePath sourceFolderResourcePath, List<ResourcePath> resources) {
		
		final File sourceFolderFile = sourceFolderResourcePath.getFile();
		
		final List<File> files = new ArrayList<>();
		
		Files.recurseDirectories(sourceFolderFile, file -> {
			
			files.add(file);
			
		});
		
		final SortedMap<String, NamespaceResourcePath> sortedMap = new TreeMap<>();

		for (File file : files) {
			final Namespace namespace = SourceFileScanner.getNamespaceResource(sourceFolderFile, file);
			
			sortedMap.put(namespace.getDirPath(), new NamespaceResourcePath(sourceFolderResourcePath, namespace.getNamespace()));
		}

		for (Map.Entry<String, NamespaceResourcePath> entry : sortedMap.entrySet()) {
			resources.add(entry.getValue());
		}
	}
}
