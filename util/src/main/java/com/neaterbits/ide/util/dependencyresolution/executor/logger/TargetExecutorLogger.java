package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;

public interface TargetExecutorLogger {

	void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState);

	void onStateChange(TargetDefinition<?> target, String oldState, String newState);
	
	void onAddRecursiveTarget(TargetDefinition<?> target, TargetDefinition<?> subTarget);
	
	void onCheckRecursiveTargetsComplete(TargetDefinition<?> target, Status status);
	
	void onAddSubRecursionCollected(TargetDefinition<?> topOfRecursionTarget, TargetDefinition<?> target, CollectedTargetObjects subTargetObjects);

	void onAddTopRecursionCollected(TargetDefinition<?> aboveRecursionTarget, Prerequisites prerequisites, CollectedTargetObjects subTargetObjects);

	void onScheduleTarget(TargetDefinition<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState);

	void onCollectProducts(TargetDefinition<?> target, CollectedProducts subProducts, CollectedProduct collected, TargetExecutorLogState logState);

	void onCollectTargetObjects(TargetDefinition<?> target, CollectedTargetObjects targetObjects, CollectedProduct collected, TargetExecutorLogState logState);
	
	void onActionCompleted(TargetDefinition<?> target, TargetExecutorLogState logState, ActionLog actionLog);

	void onActionException(TargetDefinition<?> target, TargetExecutorLogState logState, Exception exception);
	
	void onTargetDone(TargetDefinition<?> target, Exception exception, TargetExecutorLogState logState);
}
