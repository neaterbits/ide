package com.neaterbits.ide.common.build.tasks;

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
			
			.fromIterating(null, ModuleBuilderUtil::transitiveProjectDependencies)
			
			.buildBy(st -> {
				st.addFileSubTarget(
						ProjectDependency.class,
						CompiledModuleFileResourcePath.class,
						(context, dependency) -> dependency.getCompiledModuleFilePath(),
						CompiledModuleFileResourcePath::getFile,
						dependency -> "Project dependency " + dependency.getCompiledModuleFilePath().getLast().getName());
			})
			.collectSubTargetsToProduct((module, dependencies) -> new ProjectModuleDependencyList(
					module,
					dependencies));

	}
}
