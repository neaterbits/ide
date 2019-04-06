package com.neaterbits.ide.util.dependencyresolution.executor;

import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class TargetStateRecursiveTargets<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	TargetStateRecursiveTargets(Target<?> target) {
		super(target);
	}

	@Override
	Status getStatus() {
		return Status.ACTION_PERFORMED_COLLECTING_SUBTARGETS;
	}

	@Override
	public BaseTargetState<CONTEXT> onCheckPrerequisitesComplete(TargetExecutionContext<CONTEXT> context) {

		final BaseTargetState<CONTEXT> nextState;
		
		final PrerequisiteCompletion completion = hasCompletedPrerequisites(context.state, target);
		
		switch (completion.getStatus()) {
		case SUCCESS:
			Collector.collectFromSubTargetsAndSubProducts(context, target);
			onCompletedTarget(context, target, completion.getException(), false);
			nextState = new TargetStateDone<>(target);
			break;

		case FAILED:
			onCompletedTarget(context, target, completion.getException(), false);
			nextState = new TargetStateFailed<>(target, completion.getException());
			break;
			
		default:
			nextState = this;
			break;
		}

		return nextState;
	}
}
