package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.structuredlog.binary.logging.LogContext;

public final class BinaryTargetExecutorLogger implements TargetExecutorLogger {

	private final LogContext logContext;

	public BinaryTargetExecutorLogger(LogContext logContext) {

		Objects.requireNonNull(logContext);

		this.logContext = logContext;
	}

	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {

	}

	@Override
	public void onStateChange(Target<?> target, String oldState, String newState) {
		target.debug(logContext,
				"State change target " + target.targetSimpleLogString() + " from " + oldState + " to " + newState);
	}

	@Override
	public void onAddRecursiveTarget(Target<?> target, Target<?> subTarget) {

		target.debug(logContext, "Add recursive subtarget " + subTarget.targetSimpleLogString() + " from "
				+ target.targetSimpleLogString());
	}

	@Override
	public void onCheckRecursiveTargetsComplete(Target<?> target, Status status) {

		target.debug(logContext, "Check recursive subtarget complete " + target.targetSimpleLogString()
				+ " with status " + status + " from prerequisites " + target.getPrerequisites());
	}

	@Override
	public void onAddSubRecursionCollected(Target<?> topOfRecursionTarget, Target<?> target, CollectedTargetObjects subTargetObjects) {

		topOfRecursionTarget.debug(logContext, "Add recursion collected to "
				+ topOfRecursionTarget.targetSimpleLogString() + " from " + target.targetSimpleLogString() + " with objects " + subTargetObjects);

	}

	@Override
	public void onAddTopRecursionCollected(Target<?> aboveRecursionTarget, Prerequisites prerequisites, CollectedTargetObjects targetObjects) {

		aboveRecursionTarget.debug(logContext, "Top of recursion collected to " + aboveRecursionTarget.targetSimpleLogString() + " from " + targetObjects + "/" + prerequisites);
		
	}

	@Override
	public void onScheduleTarget(Target<?> target, Status status, TargetExecutorLogState logState) {

		target.debug(logContext, "Schedule target " + target.targetSimpleLogString() + ", status=" + status);

	}

	@Override
	public void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected,
			TargetExecutorLogState logState) {

		target.debug(logContext, "Collect " + collected + " to target " + target.targetSimpleLogString() + " from "
				+ subProducts.getCollectedObjects());
	}

	@Override
	public void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects,
			CollectedProduct collected, TargetExecutorLogState logState) {

		target.debug(logContext, "Collect " + collected + " to target " + target.targetSimpleLogString() + " from "
				+ targetObjects.getCollectedObjects());
	}

	@Override
	public void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog) {

		target.debug(logContext, "Action " + target.targetSimpleLogString());

	}

	@Override
	public void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception) {
		target.debug(logContext, "Action failed " + target.targetSimpleLogString());
	}

	@Override
	public void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState) {

		if (exception == null) {
			target.debug(logContext, "Complete " + target.targetSimpleLogString());
		} else {
			target.debug(logContext, "Failed " + target.targetSimpleLogString());
		}
	}

}
