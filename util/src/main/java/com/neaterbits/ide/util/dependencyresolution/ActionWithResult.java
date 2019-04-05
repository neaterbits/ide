package com.neaterbits.ide.util.dependencyresolution;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionWithResultFunction;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

final class ActionWithResult<TARGET> {

	private final Constraint constraint;
	private final ActionWithResultFunction<?, TARGET, ?> actionWithResult;
	private final ProcessResult<?, TARGET, ?> onResult;

	ActionWithResult(
			Constraint constraint,
			ActionWithResultFunction<?, TARGET, ?> actionWithResult,
			ProcessResult<?, TARGET, ?> onResult) {
		this.constraint = constraint;
		this.actionWithResult = actionWithResult;
		this.onResult = onResult;
	}

	Constraint getConstraint() {
		return constraint;
	}

	ActionWithResultFunction<?, TARGET, ?> getActionWithResult() {
		return actionWithResult;
	}

	ProcessResult<?, TARGET, ?> getOnResult() {
		return onResult;
	}
}
