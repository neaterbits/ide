package com.neaterbits.ide.common.tasks;

import java.util.Set;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.build.tasks.ModuleBuilderUtil;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilder;
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
					.fromIterating(null, ModuleBuilderUtil::transitiveProjectExternalDependencies)
					
					.buildBy(subTarget -> {
						subTarget.addFileSubTarget(
								LibraryDependency.class,
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
