package com.neaterbits.ide.util.scheduling;

public final class SynchronousSchedulerFactory implements SchedulerFactory {

	private final ForwardToCaller forwardToCaller;
	
	public SynchronousSchedulerFactory(ForwardToCaller forwardToCaller) {
		this.forwardToCaller = forwardToCaller;
	}

	@Override
	public Scheduler createScheduler() {
		return new SynchronousSchedulerImpl(forwardToCaller);
	}
}
