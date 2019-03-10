package com.neaterbits.ide.util.scheduling;

public interface AsyncExecutor extends Scheduler {

	int getNumScheduledJobs();

	void runQueuedResultRunnables();

}
