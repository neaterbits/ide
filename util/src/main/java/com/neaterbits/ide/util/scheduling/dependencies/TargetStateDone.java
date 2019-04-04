package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

final class TargetStateDone<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	TargetStateDone(Target<?> target) {
		super(target);
	}

	@Override
	Status getStatus() {
		return Status.SUCCESS;
	}
}
