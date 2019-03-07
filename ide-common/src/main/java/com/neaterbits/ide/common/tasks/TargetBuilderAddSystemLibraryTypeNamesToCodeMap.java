package com.neaterbits.ide.common.tasks;

import java.io.File;
import java.util.Set;

import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public final class TargetBuilderAddSystemLibraryTypeNamesToCodeMap extends TargetBuildSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		
		targetBuilder
			.addTarget("systemlibrarytypes", "System library type names")
			.withPrerequisites("System library files")
			.fromIterating(context -> context.getLanguage().getSystemLibraries())
			.buildBy(st -> st
				.addInfoSubTarget(
					File.class,
					"systemlibrarytypenames",
					file -> file.getName(),
					file -> "System library type names from " + file.getPath())
				.action(Constraint.IO, (context, target, parameters) -> {
					
					final Set<TypeName> types = context.getLanguage().getTypesFromSystemLibraryFile(target);

					context.getCodeMapGatherer().addSystemLibraryFileTypes(target, types);
				}));
		
	}
}
