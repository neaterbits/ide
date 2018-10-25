package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

public interface TargetExecutorLogger {

	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites);
	
	void onCollect(Target<?> target, List<Object> targetObjects, Object collected);
	
	void onAction(Target<?> target);
	
	void onComplete(Target<?> target, Exception exception);
}
