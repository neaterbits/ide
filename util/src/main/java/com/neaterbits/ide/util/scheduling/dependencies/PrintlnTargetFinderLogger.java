package com.neaterbits.ide.util.scheduling.dependencies;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.ide.util.Indent;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public class PrintlnTargetFinderLogger implements TargetFinderLogger {

	@Override
	public <CONTEXT extends TaskContext, TARGET> void onFindTarget(
			int indent,
			CONTEXT context,
			TargetSpec<CONTEXT, TARGET> targetSpec,
			TARGET target) {

		final String targetType = targetSpec.getType() != null
				? targetSpec.getType().getSimpleName()
				: "null";
		
				
		final String targetFile;
		
		if (targetSpec instanceof FileTargetSpec) {
		
			@SuppressWarnings("unchecked")
			final FileTargetSpec<CONTEXT, TARGET, ?> fileTargetSpec = (FileTargetSpec<CONTEXT, TARGET, ?>)targetSpec;
			
			if (fileTargetSpec.getFile() != null) {
				
				final Object fileTarget = fileTargetSpec.getFileTarget().apply(context, target);
				
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Function<Object, File> getFile = (Function)fileTargetSpec.getFile();
				
				targetFile = getFile.apply(fileTarget).getPath();
			}
			else {
				targetFile = "null";
			}

			System.out.println(Indent.indent(indent) + "Find file target type=" + targetType + ", file=" + targetFile);
		}
		else if (targetSpec instanceof InfoTargetSpec) {
			
			final InfoTargetSpec<CONTEXT, TARGET> infoTargetSpec = (InfoTargetSpec<CONTEXT, TARGET>)targetSpec;
			
			System.out.println(Indent.indent(indent) + "Find info target type=" + targetType + ", name=" + infoTargetSpec.getName());
		}
	}
	
	@Override
	public <CONTEXT extends TaskContext, TARGET, PREREQUISITE> void onPrerequisites(
			int indent,
			TargetSpec<CONTEXT, TARGET> targetSpec, TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec, List<Prerequisite<?>> prerequisites) {

		System.out.println(Indent.indent(indent) + "Prerequisites " + prerequisiteSpec.getDescription());
	}



	@Override
	public void onFoundPrerequisites(int indent, Target<?> target, List<Prerequisites> prerequisites) {
		System.out.println(Indent.indent(indent) + "Found target " + target + " with prerequisites " + prerequisites);
	}
}
