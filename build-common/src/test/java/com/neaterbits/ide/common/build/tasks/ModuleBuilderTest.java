package com.neaterbits.ide.common.build.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResource;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;

public class ModuleBuilderTest extends BaseBuildTest {

	private static ProjectModuleResourcePath findOneModule(BuildRoot buildRoot, String match) {
		
		return findOne(buildRoot.getModules(), ProjectModuleResourcePath::getName, match);
	}

	private static <T> T findOne(Collection<T> collection, Function<T, String> getName, String match) {
		
		 final List<T> matches = collection.stream()
					.filter(item -> getName.apply(item).contains(match))
					.collect(Collectors.toList());
			
		assertThat(matches.size()).isEqualTo(1);
		 
		return matches.get(0);
	}

	@Test
	public void testTransitiveProjectDependencies() throws ScanException {

		final BuildRoot buildRoot = getBuildRoot();
	
		for (ProjectModuleResourcePath module : buildRoot.getModules()) {
			System.out.println("## module " + module);
		}

		final ProjectModuleResourcePath root = findOneModule(buildRoot, "root");
		assertThat(root.getModuleId().getId().contains("root")).isTrue();
		
		final MavenModuleId rootModuleId = (MavenModuleId)root.getModuleId();
		
		
		final ProjectModuleResourcePath ideCommon = findOneModule(buildRoot, "ide-common");

		assertThat(ideCommon.getModuleId().getId().contains("ide-common")).isTrue();

		final List<Dependency> directDependencies = buildRoot.getDependenciesForProjectModule(ideCommon);
		
		assertThat(directDependencies).isNotNull();
		assertThat(directDependencies.isEmpty()).isFalse();
		
		for (Dependency dependency : directDependencies) {
			
			final ResourcePath resourcePath = dependency.getResourcePath();

			// System.out.println("## dependency " + dependency + "/" + resourcePath.getClass());

			assertThat(    resourcePath instanceof CompiledModuleFileResourcePath
					    || resourcePath instanceof LibraryResourcePath).isTrue();

		}
		
		// Should depend on util
		final Dependency utilDependency = findOne(
				directDependencies,
				dependency -> dependency.getResourcePath().length() <= 1
							? "" : dependency.getResourcePath().get(1).getName(),
				"util");
		
		assertThat(utilDependency.getResourcePath() instanceof CompiledModuleFileResourcePath).isTrue();
		assertThat(utilDependency.getResourcePath().get(2) instanceof CompiledModuleFileResource).isTrue();
		assertThat(utilDependency.getResourcePath().get(2).getName()).isEqualTo("util-" + rootModuleId.getVersion() + ".jar");
		/*
		
		final List<Dependency> transitiveDependencies = ModuleBuilderUtil.transitiveProjectDependencies(buildRoot, ideCommon);

		assertThat(transitiveDependencies.size()).isGreaterThan(0);

		for (Dependency dependency : transitiveDependencies) {
			System.out.println("## dependency " + dependency);
		}
		*/

	}
}
