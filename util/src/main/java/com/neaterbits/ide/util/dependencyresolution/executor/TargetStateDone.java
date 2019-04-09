package com.neaterbits.ide.util.dependencyresolution.executor;

import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class TargetStateDone<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	TargetStateDone(TargetDefinition<?> target, TargetExecutorLogger logger) {
		super(target, logger);
	}

	@Override
	Status getStatus() {
		return Status.SUCCESS;
	}
}
