package com.neaterbits.ide.common.tasks;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.Constraint;

public final class TargetBuilderAddSystemLibraryTypesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		
		targetBuilder
			.addTarget("systemlibrarytypes", "System library type names")
			.withPrerequisites("System library files")
			.fromIterating(context -> context.getCompileableLanguage().getSystemLibraries().getFiles())
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
