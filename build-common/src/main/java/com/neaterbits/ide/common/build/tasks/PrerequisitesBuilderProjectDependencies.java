package com.neaterbits.ide.common.build.tasks;

import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.ProjectDependency;
import com.neaterbits.ide.common.build.model.compile.ProjectModuleDependencyList;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.PrerequisitesBuilder;

final class PrerequisitesBuilderProjectDependencies extends PrerequisitesBuilderSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Project dependencies")
			.makingProduct(ProjectModuleDependencyList.class)
			.fromItemType(ProjectDependency.class)
			
			.fromIterating(null, (context, dependency) -> ModuleBuilderUtil.transitiveProjectDependencies(context, dependency).stream()
					.map(projectDependency -> projectDependency.getModulePath())
					.collect(Collectors.toList()))
			
			.buildBy(st -> {
				st.addFileSubTarget(
						ProjectModuleResourcePath.class,
						CompiledModuleFileResourcePath.class,
						(context, resourcePath) -> context.getBuildRoot().getCompiledModuleFile(resourcePath),
						CompiledModuleFileResourcePath::getFile,
						projectResourcePath -> "Project dependency " + projectResourcePath.getLast().getName());
			})
			.collectSubTargetsToProduct((module, dependencies) -> new ProjectModuleDependencyList(
					module,
					dependencies));

	}
}
