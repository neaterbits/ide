package com.neaterbits.ide.util.scheduling.dependencies;

public interface TargetExecutorLogger {

	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);
	
	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);

	void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected, TargetExecutorLogState logState);

	void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects, CollectedProduct collected, TargetExecutorLogState logState);
	
	void onAction(Target<?> target, TargetExecutorLogState logState);
	
	void onComplete(Target<?> target, Exception exception, TargetExecutorLogState logState);
}
