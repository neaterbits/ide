package com.neaterbits.ide.common.build.tasks;


import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.build.model.compile.ExternalModuleDependencyList;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public final class PrerequisitesBuilderExternalDependencies<CONTEXT extends TaskBuilderContext>
			extends PrerequisitesBuilderSpec<CONTEXT, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<CONTEXT, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("External dependencies")
			.makingProduct(ExternalModuleDependencyList.class)
			.fromItemType(LibraryDependency.class)
			
			.fromIteratingAndBuildingRecursively(
					Constraint.NETWORK,
					LibraryDependency.class,
					
					// from project all module target
					(context, module) -> ModuleBuilderUtil.transitiveProjectExternalDependencies(context, module),
						
					// from external dependencies found above
					(context, dep) -> ModuleBuilderUtil.transitiveLibraryDependencies(context, dep),
						
					dependency -> dependency) // already of ItemType
			
			.buildBy(st -> {
				
				st.addFileSubTarget(
						LibraryDependency.class,
						LibraryResourcePath.class,
						(context, dependency) -> dependency.getModulePath(),
						LibraryResourcePath::getFile,
						dependency -> "External dependency " + dependency.getModulePath().getLast().getName())
	
				.action(Constraint.NETWORK, (context, target, actionParams) -> {
					context.getBuildRoot().downloadExternalDependencyAndAddToBuildModel(target);
					
					return null;
				});
			})
			.collectSubTargetsToProduct((module, dependencies) -> new ExternalModuleDependencyList(module, dependencies));

	}
	
}
