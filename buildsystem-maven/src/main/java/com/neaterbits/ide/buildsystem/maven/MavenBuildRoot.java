package com.neaterbits.ide.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.xml.stream.XMLStreamException;

import com.neaterbits.ide.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.buildsystem.BuildSystemRootListener;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.language.Language;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResource;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public final class MavenBuildRoot implements BuildSystemRoot<MavenModuleId, MavenProject, MavenDependency> {
	
	private final List<MavenProject> projects;
	
	private final List<BuildSystemRootListener> listeners;
	
	MavenBuildRoot(List<MavenProject> projects) {
		
		Objects.requireNonNull(projects);

		this.listeners = new ArrayList<>();

		this.projects = projects;
	}
	
	@Override
	public Collection<MavenProject> getProjects() {
		return projects;
	}

	@Override
	public MavenModuleId getModuleId(MavenProject project) {
		
		final MavenModuleId parentModuleId = getParentModuleId(project);
		
		final MavenModuleId moduleId = project.getModuleId();
		
		return new MavenModuleId(
				moduleId.getGroupId() != null ? moduleId.getGroupId() : parentModuleId.getGroupId(),
				moduleId.getArtifactId(),
				moduleId.getVersion() != null ? moduleId.getVersion() : parentModuleId.getVersion());
	}

	@Override
	public MavenModuleId getParentModuleId(MavenProject project) {
		return project.getParentModuleId();
	}

	@Override
	public File getRootDirectory(MavenProject project) {
		return project.getRootDirectory();
	}

	@Override
	public String getNonScopedName(MavenProject project) {
		return project.getModuleId().getArtifactId();
	}

	@Override
	public String getDisplayName(MavenProject project) {
		return project.getModuleId().getArtifactId();
	}

	@Override
	public Collection<MavenDependency> getDependencies(MavenProject project) {
		return project.getDependencies();
	}

	@Override
	public Collection<MavenDependency> resolveDependencies(MavenProject project) {
		return project.resolveDependencies();
	}

	@Override
	public MavenModuleId getDependencyModuleId(MavenDependency dependency) {
		return dependency.getModuleId();
	}
	
	@Override
	public File getTargetDirectory(File modulePath) {
		return new File(modulePath, "target");
	}

	@Override
	public File getCompiledModuleFile(MavenProject project, File modulePath) {
		
		final String fileName = compiledFileName(getModuleId(project), project.getPackaging());
		
		return new File(getTargetDirectory(modulePath), fileName);
	}

	@Override
	public List<SourceFolderResourcePath> findSourceFolders(ModuleResourcePath moduleResourcePath) {

		final List<SourceFolderResourcePath> resourcePaths = new ArrayList<>();
		
		final File modulePath = moduleResourcePath.getFile();
		
		final String path = "src/main/java";
		
		final File sourcePath = new File(modulePath, path);
		
		if (sourcePath.exists()) {
			resourcePaths.add(new SourceFolderResourcePath(moduleResourcePath, new SourceFolderResource(sourcePath, path, Language.JAVA)));
		}
		
		return resourcePaths;
	}

	private String repositoryDirectory(MavenDependency mavenDependency) {

		final MavenModuleId moduleId = mavenDependency.getModuleId();

		final String path = System.getProperty("user.home") + "/.m2/repository/"
					+ moduleId.getGroupId().replace('.', '/')
					+ '/' + moduleId.getArtifactId()
					+ '/' + moduleId.getVersion();
		
		return path;
	}
	
	private File repositoryPomFile(MavenDependency mavenDependency) {
		
		final File repositoryDirectory = new File(repositoryDirectory(mavenDependency));
		final File pomFile = new File(repositoryDirectory, "pom.xml");

		return pomFile;
	}
	
	@Override
	public File repositoryJarFile(MavenDependency mavenDependency) {
		
		final String path = repositoryDirectory(mavenDependency)
					+ '/' + compiledFileName(mavenDependency);
				
		try {
			return new File(path).getCanonicalFile();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	public String compiledFileName(MavenDependency mavenDependency) {
		final MavenModuleId moduleId = mavenDependency.getModuleId();
		
		return compiledFileName(moduleId, mavenDependency.getPackaging());
	}

	private String compiledFileName(MavenModuleId moduleId, String packaging) {
		
		if (moduleId.getVersion() == null) {
			throw new IllegalArgumentException();
		}
		
		return moduleId.getArtifactId() + '-' + moduleId.getVersion()
				+ (packaging != null ? ('.' + packaging) : ".jar");
	}
	
	
	@Override
	public Collection<MavenDependency> getTransitiveExternalDependencies(MavenDependency dependency) throws ScanException {
		
		Objects.requireNonNull(dependency);
		
		final File pomFile = repositoryPomFile(dependency);

		final MavenProject mavenProject;

		try {
			 mavenProject = PomTreeParser.readModule(pomFile);
		} catch (XMLStreamException | IOException ex) {
			throw new ScanException("Failed to parse dependencies pom file", ex);
		}
		
		return mavenProject.getDependencies();
	}

	@Override
	public void downloadExternalDependencyIfNotPresent(MavenDependency dependency) {
		
		final File repositoryPomFile = repositoryPomFile(dependency);
		
		
		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(BuildSystemRootListener listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.add(listener);
	}
}
