package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionLog;

public interface TargetExecutorLogger {

	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);
	
	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);

	void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected, TargetExecutorLogState logState);

	void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects, CollectedProduct collected, TargetExecutorLogState logState);
	
	void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog);

	void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception);
	
	void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState);
}
