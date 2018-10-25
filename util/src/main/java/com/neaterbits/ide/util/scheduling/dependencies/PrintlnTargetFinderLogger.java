package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

import com.neaterbits.ide.util.Indent;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public class PrintlnTargetFinderLogger implements TargetFinderLogger {

	@Override
	public <CONTEXT extends TaskContext, TARGET, FILE_TARGET> void onFindTarget(
			int indent,
			CONTEXT context,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			TARGET target) {

		final String targetType = targetSpec.getType() != null
				? targetSpec.getType().getSimpleName()
				: "null";
		
				
		final String targetFile;
		
		if (targetSpec.getFile() != null) {
			
			final FILE_TARGET fileTarget = targetSpec.getFileTarget().apply(context, target);
			
			targetFile = targetSpec.getFile().apply(fileTarget).getPath();
		}
		else {
			targetFile = "null";
		}
		
		System.out.println(Indent.indent(indent) + "Find target type=" + targetType + ", name=" + targetSpec.getName() + ", file=" + targetFile);
	}
	
	@Override
	public <CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE> void onPrerequisites(
			int indent,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec, TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec, List<Prerequisite<?>> prerequisites) {

		System.out.println(Indent.indent(indent) + "Prerequisites " + prerequisiteSpec.getDescription());
	}



	@Override
	public void onFoundPrerequisites(int indent, Target<?> target, List<Prerequisites> prerequisites) {
		System.out.println(Indent.indent(indent) + "Found target " + target + " with prerequisites " + prerequisites);
	}
}
