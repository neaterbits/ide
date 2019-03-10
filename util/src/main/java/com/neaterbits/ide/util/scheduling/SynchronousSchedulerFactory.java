package com.neaterbits.ide.util.scheduling;

public final class SynchronousSchedulerFactory implements SchedulerFactory {

	private final ForwardResultToCaller forwardToCaller;
	
	public SynchronousSchedulerFactory(ForwardResultToCaller forwardToCaller) {
		this.forwardToCaller = forwardToCaller;
	}

	@Override
	public Scheduler createScheduler() {
		return new SynchronousSchedulerImpl(forwardToCaller);
	}
}
