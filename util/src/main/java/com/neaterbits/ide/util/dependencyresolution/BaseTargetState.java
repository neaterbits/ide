package com.neaterbits.ide.util.dependencyresolution;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.ide.util.statemachine.BaseState;

public abstract class BaseTargetState<CONTEXT extends TaskContext>
		extends BaseState<BaseTargetState<CONTEXT>>
		implements TargetOps<CONTEXT> {

	final Target<?> target;

	abstract Status getStatus();
	
	public BaseTargetState(Target<?> target) {
		Objects.requireNonNull(target);
		
		this.target = target;
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
	
	static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(ExecutorState<CONTEXT> targetState, Target<?> target) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				
				if (prerequisite.getSubTarget() != null) {
					
					final PrerequisiteCompletion subStatus = hasCompletedPrerequisites(targetState, prerequisite.getSubTarget());
					
					if (subStatus.getStatus() != Status.SUCCESS) {
						// System.out.println("## missing substatus " + prerequisite.getSubTarget() + "/" + subStatus);

						return subStatus;
					}

					return targetState.getTargetCompletion(prerequisite.getSubTarget());
				}
			}
		}
		
		return new PrerequisiteCompletion(Status.SUCCESS);
	}
}
