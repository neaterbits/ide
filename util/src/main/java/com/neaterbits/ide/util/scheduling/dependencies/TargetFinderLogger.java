package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public interface TargetFinderLogger {

	<CONTEXT extends TaskContext, TARGET, FILE_TARGET>
	void onFindTarget(int indent, CONTEXT context, TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec, TARGET target);
	
	void onFoundPrerequisites(int indent, Target<?> target, List<Prerequisites> prerequisites);
	
	<CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE>
	void onPrerequisites(
			int indent,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			List<Prerequisite<?>> prerequisites);
}
