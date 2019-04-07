package com.neaterbits.ide.util.dependencyresolution.spec;

import java.util.List;
import java.util.Set;

import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

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
			Set<Prerequisite<?>> prerequisites);
}
