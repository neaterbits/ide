package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

public interface TargetExecutorLogger {

	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);
	
	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);
	
	void onCollect(Target<?> target, List<Object> targetObjects, Object collected, TargetExecutorLogState logState);
	
	void onAction(Target<?> target, TargetExecutorLogState logState);
	
	void onComplete(Target<?> target, Exception exception, TargetExecutorLogState logState);
}
