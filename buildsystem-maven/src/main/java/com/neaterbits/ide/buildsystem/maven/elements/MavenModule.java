package com.neaterbits.ide.buildsystem.maven.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

public class MavenModule extends MavenEntity {

	private final File rootDirectory;
	private final MavenModuleId parentModuleId;
	
	private final List<String> subModules;
	private final List<MavenDependency> dependencies;
	private final MavenBuild build;
	
	public MavenModule(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenModuleId parentModuleId,
			String packaging,
			List<String> subModules,
			List<MavenDependency> dependencies,
			MavenBuild build) {

		super(moduleId, packaging);
		
		Objects.requireNonNull(rootDirectory);
		
		this.rootDirectory = rootDirectory;

		this.parentModuleId = parentModuleId;
		
		this.subModules = subModules;
		this.dependencies = dependencies;
		this.build = build;
	}
	
	private String getGroupId() {
		return getModuleId().getGroupId() != null
				? getModuleId().getGroupId()
				: getParentModuleId().getGroupId();
						
	}
	
	private String getVersion() {
		return getModuleId().getVersion() != null
				? getModuleId().getVersion()
				: getParentModuleId().getVersion();
	}
	
	public final File getRootDirectory() {
		return rootDirectory;
	}

	public final MavenModuleId getParentModuleId() {
		return parentModuleId;
	}

	public final List<String> getSubModules() {
		return subModules;
	}

	public final List<MavenDependency> getDependencies() {
		return dependencies;
	}
	
	public final List<MavenDependency> resolveDependencies() {
		
		final List<MavenDependency> resolvedDependencies;
		
		if (dependencies == null) {
			resolvedDependencies = null;
		}
		else {
			resolvedDependencies = new ArrayList<>(dependencies.size());
			
			for (MavenDependency dependency : dependencies) {
				final MavenDependency resolved = new MavenDependency(
						new MavenModuleId(
								dependency.getModuleId().getGroupId().replace("${project.groupId}", getGroupId()),
								dependency.getModuleId().getArtifactId(),
								dependency.getModuleId().getVersion().replace("${project.version}", getVersion())),
						
						dependency.getPackaging());
				
				resolvedDependencies.add(resolved);
			}
		}

		return resolvedDependencies;
	}

	public final MavenBuild getBuild() {
		return build;
	}
}
