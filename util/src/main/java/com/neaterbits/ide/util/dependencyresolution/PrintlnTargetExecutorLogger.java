package com.neaterbits.ide.util.dependencyresolution;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionLog;

public final class PrintlnTargetExecutorLogger implements TargetExecutorLogger {

	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {

		System.out.println("Schedule target numScheduledJobs=" + numScheduledJobs);
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
