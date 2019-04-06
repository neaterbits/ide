package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.function.BiConsumer;

import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class TargetStateToExecute<CONTEXT extends TaskContext>
			extends BaseTargetState<CONTEXT> implements TargetOps<CONTEXT> {

	public TargetStateToExecute(Target<?> target) {
		super(target);
	}
	
	@Override
	Status getStatus() {
		return Status.TO_EXECUTE;
	}

	@Override
	public BaseTargetState<CONTEXT> onCheckPrerequisitesComplete(TargetExecutionContext<CONTEXT> context) {
		
		final PrerequisiteCompletion status = hasCompletedPrerequisites(context.state, target);
		
		final BaseTargetState<CONTEXT> nextState;
		
		if (status.getStatus() == Status.SUCCESS) {
			
			if (context.logger != null) {
				context.logger.onScheduleTarget(target, status.getStatus(), context.state);
			}

			Collector.collectFromSubTargetsAndSubProducts(context, target);

			final boolean hasActions = runAnyActionsAndCallOnCompleted(context, target, (exception, async) -> {
				schedule(state -> exception != null
						? state.onActionError(context, exception)
						: state.onActionPerformed(context));
			});
			
			if (hasActions) {
				nextState = new TargetStatePerformingActions<>(target);
			}
			else {
				
				onCompletedTarget(context, target, null, false);
				
				nextState = new TargetStateDone<>(target);
			}
		}
		else if (status.getStatus() == Status.FAILED) {
			// context.state.moveTargetFromToExecuteToFailed(target);
			
			if (context.logger != null) {
				context.logger.onTargetDone(target, status.getException(), context.state);
			}
			
			onCompletedTarget(context, target, status.getException(), false);
			nextState = new TargetStateFailed<>(target, status.getException());
		}
		else {
			nextState = this;
		}

		return nextState;
	}
	private boolean runAnyActionsAndCallOnCompleted(
			TargetExecutionContext<CONTEXT> context, Target<?> target,
			BiConsumer<Exception, Boolean> onCompleted) {

		final Action<?> action = target.getAction();
		final ActionWithResult<?> actionWithResult = target.getActionWithResult();
		
		final boolean actionRun;
		
		
		if (action != null) {
			Actions.runOrScheduleAction(context, action, target, onCompleted);
			
			actionRun = true;
		}
		else if (actionWithResult != null) {
			Actions.runOrScheduleActionWithResult(context, actionWithResult, target, onCompleted);
			
			actionRun = true;
			
		}
		else {
			actionRun = false;
		}
		
		return actionRun;
	}
}

