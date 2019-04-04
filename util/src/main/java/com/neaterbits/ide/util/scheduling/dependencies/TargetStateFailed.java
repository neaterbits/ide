package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public class TargetStateFailed<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	private final Exception exception;

	TargetStateFailed(Target<?> target, Exception exception) {
		super(target);

		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}

	@Override
	Status getStatus() {
		return Status.FAILED;
	}
}
