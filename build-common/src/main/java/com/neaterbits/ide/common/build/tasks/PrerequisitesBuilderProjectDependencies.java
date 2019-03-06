package com.neaterbits.ide.common.build.tasks;

import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.DependencyType;
import com.neaterbits.ide.common.build.model.compile.ProjectModuleDependencyList;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisitesBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.PrerequisitesBuilder;

final class PrerequisitesBuilderProjectDependencies extends PrerequisitesBuildSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Project dependencies")
			.makingProduct(ProjectModuleDependencyList.class)
			.fromItemType(Dependency.class)
			
			.fromIterating(null, (context, module) -> ModuleBuilderUtil.transitiveProjectDependencies(context, module).stream()
					.filter(dependency -> dependency.getType() == DependencyType.PROJECT)
					.collect(Collectors.toList()))
			
			.buildBy(st -> {
				st.addFileSubTarget(
						Dependency.class,
						CompiledModuleFileResourcePath.class,
						(context, dependency) -> (CompiledModuleFileResourcePath)dependency.getResourcePath(),
						CompiledModuleFileResourcePath::getFile,
						dependency -> "Project dependency " + dependency.getResourcePath().getLast().getName());
			})
			.collectSubTargetsToProduct((module, dependencies) -> new ProjectModuleDependencyList(
					module,
					dependencies.stream()
						.filter(dependency -> dependency.getType() == DependencyType.PROJECT)
						.collect(Collectors.toList())));

	}
}
