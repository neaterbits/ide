package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.ide.util.statemachine.BaseState;

public abstract class BaseTargetState<CONTEXT extends TaskContext>
		extends BaseState<BaseTargetState<CONTEXT>>
		implements TargetOps<CONTEXT> {

	final Target<?> target;
	final TargetExecutorLogger logger;

	abstract Status getStatus();
	
	Exception getException() {
		throw new IllegalStateException();
	}
	
	public BaseTargetState(Target<?> target, TargetExecutorLogger logger) {
		Objects.requireNonNull(target);
		
		this.target = target;
		this.logger = logger;
	}
	
	@Override
	public BaseTargetState<CONTEXT> onCheckPrerequisitesComplete(TargetExecutionContext<CONTEXT> context) {
		throw new IllegalStateException("not implemented in state " + getClass().getSimpleName());
	}

	@Override
	public BaseTargetState<CONTEXT> onActionPerformed(TargetExecutionContext<CONTEXT> context) {
		throw new IllegalStateException();
	}
	
	@Override
	public BaseTargetState<CONTEXT> onActionError(TargetExecutionContext<CONTEXT> context, Exception ex) {
		throw new IllegalStateException();
	}

	@Override
	public BaseTargetState<CONTEXT> onActionWithResultPerformed() {
		throw new IllegalStateException();
	}

	final void onCompletedTarget(
			TargetExecutionContext<CONTEXT> context,
			Target<?> target,
			Exception exception,
			boolean async) {

		
		if (context.logger != null) {
			context.logger.onTargetDone(target, exception, context.state);
		}
		
		// context.state.onCompletedTarget(target, exception);

		context.state.onCompletedTarget(target);
	}
}
