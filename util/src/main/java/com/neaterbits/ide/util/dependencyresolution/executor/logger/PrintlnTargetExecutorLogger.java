package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;

public final class PrintlnTargetExecutorLogger implements TargetExecutorLogger {

	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {

		System.out.println("Schedule target numScheduledJobs=" + numScheduledJobs);
	}

	@Override
	public void onStateChange(Target<?> target, String oldState, String newState) {

		System.out.println("State change target " + target.targetToLogString() + " from " + oldState + " to " + newState);
	}

	@Override
	public void onAddRecursiveTarget(Target<?> target, Target<?> subTarget) {

		System.out.println("Add recursive subtarget " + subTarget + " from " + target);
	}

	@Override
	public void onCheckRecursiveTargetsComplete(Target<?> target, Status status) {

		System.out.println("Check recursive subtarget complete " + target + " with status " + status
				+ " from prerequisites " + target.getPrerequisites());
		
	}

	@Override
	public void onAddSubRecursionCollected(Target<?> topOfRecursionTarget, CollectedTargetObjects subTargetObjects) {

		System.out.println("Add recursion collected to " + topOfRecursionTarget.targetSimpleLogString() + " from " + subTargetObjects);
		
	}

	@Override
	public void onAddTopRecursionCollected(Target<?> aboveRecursionTarget, Prerequisites prerequisites, CollectedTargetObjects targetObjects) {

		System.out.println("Top of recursion collected to " + aboveRecursionTarget.targetSimpleLogString() + " from " + targetObjects + "/" + prerequisites);
		
	}

	@Override
	public void onScheduleTarget(Target<?> target, Status status, TargetExecutorLogState logState) {

		System.out.println("Schedule target " + target.targetToLogString() + ", status=" + status);
		
	}
	
	@Override
	public void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected,
			TargetExecutorLogState logState) {

		System.out.println("Collect " + collected + " to target " + target.targetToLogString() + " from " + subProducts.getCollectedObjects());
	}

	@Override
	public void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects,
			CollectedProduct collected, TargetExecutorLogState logState) {

		System.out.println("Collect " + collected + " to target " + target.targetToLogString() + " from " + targetObjects.getCollectedObjects());
	}


	@Override
	public void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog) {

		System.out.println("Action " + target);
		
	}
	
	

	@Override
	public void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception) {
		System.out.println("Action failed " + target);
	}

	@Override
	public void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
		if (exception == null) {
			System.out.println("Complete " + target.targetToLogString());
		}
		else {
			System.out.println("Failed " + target.targetToLogString());
			
			exception.printStackTrace();
		}
	}
}
