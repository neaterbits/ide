package com.neaterbits.ide.common.tasks;

import java.util.Set;

import com.neaterbits.build.common.tasks.ModuleBuilderUtil;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

// Add type names from eg. jar file index
public final class TargetBuilderAddLibraryTypesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(
		        "librariescodemap",
		        "libraries_codemap",
		        "build_codemap",
		        "External libraries code map")
			.withPrerequisites("modules")
			.fromIterating(InitialScanContext::getModules)
			
			.buildBy(st -> st.addInfoSubTarget(
						ProjectModuleResourcePath.class,
						"module",
						"module_scan",
						module -> module.getName(),
						module -> "Scan module " + module.getName())
					.withPrerequisites("Scan any found libraries")
					.fromIterating(
					        null,
					        (context, module) ->
					            ModuleBuilderUtil.transitiveProjectExternalDependencies(
					                                context.getBuildRoot(),
					                                module))
					
					.buildBy(subTarget -> {
						subTarget.addFileSubTarget(
								LibraryDependency.class,
								"library_dependency" ,
								"gather_library_dependency",
								LibraryResourcePath.class,
								(context, dependency) -> dependency.getModulePath(),
								LibraryResourcePath::getFile,
								dependency -> "Project dependency " + dependency.getModulePath().getLast().getName())
						
						.action(Constraint.IO, (context, target, parameters) -> {
							
							final LibraryResourcePath libraryResourcePath = target.getModulePath();
							final Set<TypeName> types = context.getCompileableLanguage().getTypesFromLibraryFile(libraryResourcePath);
							
							context.getCodeMapGatherer().addLibraryFileTypes(libraryResourcePath, types);
							
							return null;
						});
					}));
		
	}
}
