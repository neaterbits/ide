package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;

public final class DelegatingTargetExecutorLogger implements TargetExecutorLogger {

	private final List<TargetExecutorLogger> delegates;

	public DelegatingTargetExecutorLogger(TargetExecutorLogger ... delegates) {
		this.delegates = Arrays.asList(delegates);
	}
	
	@Override
	public void onStateChange(Target<?> target, String oldState, String newState) {
		delegates.forEach(logger -> logger.onStateChange(target, oldState, newState));
	}

	@Override
	public void onAddRecursiveTarget(Target<?> target, Target<?> subTarget) {
		delegates.forEach(logger -> logger.onAddRecursiveTarget(target, subTarget));
	}

	@Override
	public void onCheckRecursiveTargetsComplete(Target<?> target, Status status) {
		delegates.forEach(logger -> logger.onCheckRecursiveTargetsComplete(target, status));
	}

	@Override
	public void onAddSubRecursionCollected(Target<?> topOfRecursionTarget, Target<?> target, CollectedTargetObjects subTargetObjects) {
		delegates.forEach(logger -> logger.onAddSubRecursionCollected(topOfRecursionTarget, target, subTargetObjects));
	}

	@Override
	public void onAddTopRecursionCollected(Target<?> aboveRecursionTarget, Prerequisites prerequisites, CollectedTargetObjects subTargetObjects) {
		delegates.forEach(logger -> logger.onAddTopRecursionCollected(aboveRecursionTarget, prerequisites, subTargetObjects));
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
