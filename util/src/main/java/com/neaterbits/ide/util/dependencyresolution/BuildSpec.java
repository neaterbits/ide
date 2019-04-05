package com.neaterbits.ide.util.dependencyresolution;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public final class BuildSpec<CONTEXT extends TaskContext, PREREQUISITE> {

	private final TargetSpec<CONTEXT, PREREQUISITE> subTarget;

	public BuildSpec(TargetSpec<CONTEXT, PREREQUISITE> subTarget) {
		
		Objects.requireNonNull(subTarget);
		
		this.subTarget = subTarget;
	}

	public TargetSpec<CONTEXT, PREREQUISITE> getSubTarget() {
		return subTarget;
	}
}
