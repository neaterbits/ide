package com.neaterbits.ide.common.tasks;

import java.util.List;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.dependencyresolution.builder.ActionResult;
import com.neaterbits.ide.util.dependencyresolution.builder.TargetBuilder;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.ide.util.scheduling.Constraint;

public class TargetBuilderGetSourceFolders extends TargetBuilderSpec<InitialScanContext> {

	public static final String NAME = "sourcefolders";
	
	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(NAME, "Source folders for all modules")
			.withPrerequisites("Source folders")
			.fromIterating(InitialScanContext::getModules)
			.buildBy(subTarget -> subTarget
					.addInfoSubTarget(
							ProjectModuleResourcePath.class,
							"sourcefolders",
							ProjectModuleResourcePath::getName,
							module -> "Find source folders for " + module.getName())
					
					.actionWithResult(Constraint.IO, (context, module) ->  {

						final List<SourceFolderResourcePath> result = context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module);
						
						return new ActionResult<List<SourceFolderResourcePath>>(result, null);
					})
					.processResult((context, module, sourceFolders) -> context.getBuildRoot().setSourceFolders(module, sourceFolders))
			);
			
	}
}
