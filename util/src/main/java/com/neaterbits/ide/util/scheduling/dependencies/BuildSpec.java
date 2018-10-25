package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public final class BuildSpec<CONTEXT extends TaskContext, PREREQUISITE> {

	private final TargetSpec<CONTEXT, PREREQUISITE, ?> subTarget;

	public BuildSpec(TargetSpec<CONTEXT, PREREQUISITE, ?> subTarget) {
		
		Objects.requireNonNull(subTarget);
		
		this.subTarget = subTarget;
	}

	public TargetSpec<CONTEXT, PREREQUISITE, ?> getSubTarget() {
		return subTarget;
	}
}
