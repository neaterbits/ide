package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;

public interface TargetExecutorLogger {

	
	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);

	void onStateChange(Target<?> target, String oldState, String newState);
	
	void onAddRecursiveTarget(Target<?> target, Target<?> subTarget);
	
	void onCheckRecursiveTargetsComplete(Target<?> target, Status status);
	
	void onAddSubRecursionCollected(Target<?> topOfRecursionTarget, Target<?> target, CollectedTargetObjects subTargetObjects);

	void onAddTopRecursionCollected(Target<?> aboveRecursionTarget, Prerequisites prerequisites, CollectedTargetObjects subTargetObjects);

	void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);

	void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected, TargetExecutorLogState logState);

	void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects, CollectedProduct collected, TargetExecutorLogState logState);
	
	void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog);

	void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception);
	
	void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState);
}
