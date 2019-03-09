package com.neaterbits.ide.common.tasks;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.TargetBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;

public final class TargetBuilderAddSystemLibraryTypesToCodeMap extends TargetBuildSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		
		targetBuilder
			.addTarget("systemlibrarytypes", "System library type names")
			.withPrerequisites("System library files")
			.fromIterating(context -> context.getLanguage().getSystemLibraries().getFiles())
			.buildBy(st -> st
				.addInfoSubTarget(
					DependencyFile.class,
					"systemlibrarytypenames",
					file -> file.getFile().getName(),
					file -> "System library type names from " + file.getFile().getPath())
				.action(Constraint.IO, (context, target, parameters) -> {
					
					context.getCodeMapGatherer().addSystemLibraryFile(target);
					
					return null;
				}));
		
	}
}
