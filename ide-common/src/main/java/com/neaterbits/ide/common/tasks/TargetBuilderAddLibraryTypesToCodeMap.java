package com.neaterbits.ide.common.tasks;

import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.build.model.Dependency;
import com.neaterbits.ide.common.build.model.DependencyType;
import com.neaterbits.ide.common.build.tasks.ModuleBuilderUtil;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.dependencyresolution.TargetBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

// Add type names from eg. jar file index
public final class TargetBuilderAddLibraryTypesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget("librariescodemap", "External libraries code map")
			.withPrerequisites("modules")
			.fromIterating(InitialScanContext::getModules)
			
			.buildBy(st -> st.addInfoSubTarget(
						ProjectModuleResourcePath.class,
						"scan",
						module -> module.getName(),
						module -> "Scan module " + module.getName())
					.withPrerequisites("Scan any found libraries")
					.fromIterating(null, (context, module) -> ModuleBuilderUtil.transitiveExternalDependencies(context, module).stream()
							.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
							.collect(Collectors.toList()))
					
					.buildBy(subTarget -> {
						subTarget.addFileSubTarget(
								Dependency.class,
								LibraryResourcePath.class,
								(context, dependency) -> (LibraryResourcePath)dependency.getResourcePath(),
								LibraryResourcePath::getFile,
								dependency -> "Project dependency " + dependency.getResourcePath().getLast().getName())
						
						.action(Constraint.IO, (context, target, parameters) -> {
							
							final LibraryResourcePath libraryResourcePath = (LibraryResourcePath)target.getResourcePath();
							final Set<TypeName> types = context.getCompileableLanguage().getTypesFromLibraryFile(libraryResourcePath);
							
							context.getCodeMapGatherer().addLibraryFileTypes(libraryResourcePath, types);
							
							return null;
						});
					}));
		
	}
}
