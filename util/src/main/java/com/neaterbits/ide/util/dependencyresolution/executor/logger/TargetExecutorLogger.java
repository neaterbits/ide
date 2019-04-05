package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionLog;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.executor.Target;

public interface TargetExecutorLogger {

	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);
	
	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);

	void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected, TargetExecutorLogState logState);

	void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects, CollectedProduct collected, TargetExecutorLogState logState);
	
	void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog);

	void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception);
	
	void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState);
}
