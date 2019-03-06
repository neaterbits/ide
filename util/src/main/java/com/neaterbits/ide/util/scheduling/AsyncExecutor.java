package com.neaterbits.ide.util.scheduling;

public final class AsyncExecutor implements Scheduler {
	
	private final ThreadsafeQueue<Runnable> queue;
	private final Scheduler scheduler;

	private int scheduledJobs;
	
	public AsyncExecutor(boolean scheduleAsynchronously) {
		this.queue = new ThreadsafeQueue<>();

		final ForwardToCaller forwardToCaller = runnable -> queue.add(runnable);

		final SchedulerFactory schedulerFactory =
				scheduleAsynchronously
					? new AsynchronousSchedulerFactory(forwardToCaller)
					: new SynchronousSchedulerFactory(forwardToCaller);

		this.scheduler = schedulerFactory.createScheduler();
		
	}

	@Override
	public <T, R> void schedule(Constraint constraint, T parameter, ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener) {

		++ scheduledJobs;

		scheduler.schedule(constraint, parameter, function, listener);
	}

	public int getNumScheduledJobs() {
		return scheduledJobs;
	}
	
	public final void runQueuedRunnables() {

		Runnable runnable;

		while (scheduledJobs > 0) {

			while (null != (runnable = queue.take())) {
				
				-- scheduledJobs;
				
				runnable.run();
				
				if (scheduledJobs == 0) {
					
					if (!queue.isEmpty()) {
						throw new IllegalStateException();
					}
					break;
				}
			}
		}
	}
}
