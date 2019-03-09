package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public interface TargetFinderLogger {

	<CONTEXT extends TaskContext, TARGET>
	void onFindTarget(int indent, CONTEXT context, TargetSpec<CONTEXT, TARGET> targetSpec, TARGET target);
	
	void onFoundPrerequisites(int indent, Target<?> target, List<Prerequisites> prerequisites);
	
	<CONTEXT extends TaskContext, TARGET, PREREQUISITE>
	void onPrerequisites(
			int indent,
			TargetSpec<CONTEXT, TARGET> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			List<Prerequisite<?>> prerequisites);
}
