package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionLog;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.executor.Target;
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
	public void onScheduleTarget(Target<?> target, Status status, TargetExecutorLogState logState) {

		target.debug(logContext, "Schedule target " + target.targetToLogString() + ", status=" + status);
		
	}
	
	@Override
	public void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected,
			TargetExecutorLogState logState) {

		target.debug(logContext, "Collect " + collected + " to target " + target.targetToLogString() + " from " + subProducts.getCollectedObjects());
	}

	@Override
	public void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects,
			CollectedProduct collected, TargetExecutorLogState logState) {

		target.debug(logContext, "Collect " + collected + " to target " + target.targetToLogString() + " from " + targetObjects.getCollectedObjects());
	}

	@Override
	public void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog) {

		target.debug(logContext, "Action " + target);
		
	}
	
	@Override
	public void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception) {
		target.debug(logContext, "Action failed " + target);
	}

	@Override
	public void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
		if (exception == null) {
			target.debug(logContext, "Complete " + target.targetToLogString());
		}
		else {
			target.debug(logContext, "Failed " + target.targetToLogString());
		}
	}

}
