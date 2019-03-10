package com.neaterbits.ide.util.scheduling;

import com.neaterbits.ide.util.scheduling.cpu.CPUScheduler;
import com.neaterbits.ide.util.scheduling.cpu.ThreadCPUScheduler;
import com.neaterbits.ide.util.scheduling.io.ThreadIOSchedulerImpl;

public class AsynchronousSchedulerFactory implements SchedulerFactory {

	private final ThreadIOSchedulerImpl ioScheduler;
	private final CPUScheduler cpuScheduler;
	
	public AsynchronousSchedulerFactory(ForwardResultToCaller forwardToCaller) {
		this.ioScheduler = new ThreadIOSchedulerImpl(forwardToCaller);
		this.cpuScheduler = new ThreadCPUScheduler(forwardToCaller);
	}
	
	@Override
	public Scheduler createScheduler() {
		return new AsynchronousSchedulerImpl(ioScheduler, cpuScheduler);
	}
}
