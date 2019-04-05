package com.neaterbits.ide.util.dependencyresolution.executor;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class TargetStateDone<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	TargetStateDone(Target<?> target) {
		super(target);
	}

	@Override
	Status getStatus() {
		return Status.SUCCESS;
	}
}
