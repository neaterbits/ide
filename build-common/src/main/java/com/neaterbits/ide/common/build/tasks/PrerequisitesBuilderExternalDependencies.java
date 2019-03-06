package com.neaterbits.ide.common.build.tasks;

import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.DependencyType;
import com.neaterbits.ide.common.build.model.compile.ExternalModuleDependencyList;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisitesBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.PrerequisitesBuilder;

final class PrerequisitesBuilderExternalDependencies extends PrerequisitesBuildSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		
		builder
			.withPrerequisites("External dependencies")
			.makingProduct(ExternalModuleDependencyList.class)
			.fromItemType(Dependency.class)
			
			.fromIteratingAndBuildingRecursively(
					Constraint.NETWORK,
					Dependency.class,
					
					// from project all module target
					(context, module) -> ModuleBuilderUtil.transitiveProjectDependencies(context, module).stream()
						.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
						.collect(Collectors.toList()),
						
					// from external dependencies found above
					(context, dep) -> ModuleBuilderUtil.transitiveDependencies(context, dep).stream()
						.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
						.collect(Collectors.toList()),
						
					dependency -> dependency // dependency -> (ModuleResourcePath)dependency.getResourcePath()
					)
			
			.buildBy(st -> {
				
				st.addFileSubTarget(
						Dependency.class,
						LibraryResourcePath.class,
						(context, dependency) -> (LibraryResourcePath)dependency.getResourcePath(),
						LibraryResourcePath::getFile,
						dependency -> "External dependency " + dependency.getResourcePath().getLast().getName())
	
				.action(Constraint.NETWORK, (context, target, actionParams) -> {
					context.getBuildRoot().downloadExternalDependencyAndAddToBuildModel(target);
				});
			})
			.collectSubTargetsToProduct((module, dependencies) -> new ExternalModuleDependencyList(
					module,
					dependencies.stream()
						.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
						.collect(Collectors.toList())));

	}
	
}
