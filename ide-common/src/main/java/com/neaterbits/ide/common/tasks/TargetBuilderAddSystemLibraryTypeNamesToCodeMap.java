package com.neaterbits.ide.common.tasks;

import java.util.Set;

import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public final class TargetBuilderAddSystemLibraryTypeNamesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		
		targetBuilder
			.addTarget(
			        "systemlibrarytypes",
			        "system_libraries_typenames_to_codemap",
			        "scan_system_libraries_for_typenames",
			        "System library type names")
			.withPrerequisites("System library files")
			.fromIterating(context -> context.getCompileableLanguage().getSystemLibraries().getFiles())
			.buildBy(st -> st
				.addInfoSubTarget(
					DependencyFile.class,
					"system_library_typenames",
					"scan_system_library_for_typenames",
					file -> file.getFile().getName(),
					file -> "System library type names from " + file.getFile().getPath())
				.action(Constraint.IO, (context, target, parameters) -> {
					
					final Set<TypeName> types = context.getCompileableLanguage().getTypesFromSystemLibraryFile(target);

					context.getCodeMapGatherer().addSystemLibraryFileTypes(target.getFile(), types);
					
					return null;
				}));
		
	}
}
