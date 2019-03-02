package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

public final class PrintlnTargetExecutorLogger implements TargetExecutorLogger {

	@Override
	public void onScheduleTarget(Target<?> target, Status status, TargetExecutorLogState logState) {

		System.out.println("Schedule target " + target.targetToLogString() + ", status=" + status);
		
	}

	@Override
	public void onCollect(Target<?> target, List<Object> targetObjects, Object collected, TargetExecutorLogState logState) {

		System.out.println("Collect " + collected + " to target " + target.targetToLogString() + " from " + targetObjects);
		
	}

	@Override
	public void onAction(Target<?> target, TargetExecutorLogState logState) {

		System.out.println("Action " + target);
		
	}

	@Override
	public void onComplete(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
		if (exception == null) {
			System.out.println("Complete " + target.targetToLogString());
		}
		else {
			System.out.println("Failed " + target.targetToLogString());
			
			exception.printStackTrace();
		}
	}
}
