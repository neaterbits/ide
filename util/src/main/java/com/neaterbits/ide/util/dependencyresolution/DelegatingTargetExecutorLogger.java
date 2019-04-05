package com.neaterbits.ide.util.dependencyresolution;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionLog;

public final class DelegatingTargetExecutorLogger implements TargetExecutorLogger {

	private final List<TargetExecutorLogger> delegates;

	public DelegatingTargetExecutorLogger(TargetExecutorLogger ... delegates) {
		this.delegates = Arrays.asList(delegates);
	}
	
	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {
		delegates.forEach(logger -> logger.onScheduleTargets(numScheduledJobs, logState));
	}

	@Override
	public void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState) {
		delegates.forEach(logger -> logger.onScheduleTarget(target, hasCompletedPrerequisites, logState));
	}

	@Override
	public void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected,
			TargetExecutorLogState logState) {

		delegates.forEach(logger -> logger.onCollectProducts(target, subProducts, collected, logState));
	}

	@Override
	public void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects,
			CollectedProduct collected, TargetExecutorLogState logState) {

		delegates.forEach(logger -> logger.onCollectTargetObjects(target, targetObjects, collected, logState));
	}

	@Override
	public void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog) {

		delegates.forEach(logger -> logger.onActionCompleted(target, logState, actionLog));
	}

	@Override
	public void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception) {

		delegates.forEach(logger -> logger.onActionException(target, logState, exception));
	}

	@Override
	public void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState) {

		delegates.forEach(logger -> logger.onTargetDone(target, exception, logState));
	}
}