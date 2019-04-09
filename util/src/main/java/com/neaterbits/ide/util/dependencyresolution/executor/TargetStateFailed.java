package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public class TargetStateFailed<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	private final Exception exception;

	TargetStateFailed(TargetDefinition<?> target, TargetExecutorLogger logger, Exception exception) {
		super(target, logger);

		Objects.requireNonNull(exception);
		
		this.exception = exception;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	Status getStatus() {
		return Status.FAILED;
	}
}
