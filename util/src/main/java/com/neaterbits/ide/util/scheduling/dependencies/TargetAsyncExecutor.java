package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.ForwardToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;
import com.neaterbits.ide.util.scheduling.ScheduleListener;
import com.neaterbits.ide.util.scheduling.Scheduler;
import com.neaterbits.ide.util.scheduling.SchedulerFactory;
import com.neaterbits.ide.util.scheduling.SynchronousSchedulerFactory;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.ThreadsafeQueue;

abstract class TargetAsyncExecutor {

	private final ThreadsafeQueue<Runnable> queue;
	final Scheduler scheduler;

	private int scheduledJobs;
	
	TargetAsyncExecutor() {
		this.queue = new ThreadsafeQueue<>();

		final ForwardToCaller forwardToCaller = runnable -> queue.add(runnable);

		final SchedulerFactory schedulerFactory = 
				new SynchronousSchedulerFactory(forwardToCaller);
				// new AsynchronousSchedulerFactory(forwardToCaller);

		final Scheduler scheduler = schedulerFactory.createScheduler();
		
		this.scheduler = new Scheduler() {
			@Override
			public <T, R> void schedule(
					Constraint constraint,
					T parameter,
					ScheduleFunction<T, R> function,
					ScheduleListener<T, R> listener) {
				
				++ scheduledJobs;

				scheduler.schedule(constraint, parameter, function, listener);
			}
		};
	}

	final void runQueuedRunnables() {

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
