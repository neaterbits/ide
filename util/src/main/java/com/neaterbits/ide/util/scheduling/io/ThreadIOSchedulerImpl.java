package com.neaterbits.ide.util.scheduling.io;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.ForwardResultToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;
import com.neaterbits.ide.util.scheduling.ScheduleListener;

public final class ThreadIOSchedulerImpl {
	
	private final ForwardResultToCaller forwardToCaller;
	
	private final ThreadIOQueue<Object, Object> queue;

	private Thread thread;
	
	public ThreadIOSchedulerImpl(ForwardResultToCaller forwardToCaller) {
		
		Objects.requireNonNull(forwardToCaller);

		this.forwardToCaller = forwardToCaller;

		this.queue = new ThreadIOQueue<>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T, R> void schedule(T parameter, String debugName, ScheduleFunction<T, R> performIO, ScheduleFunction<T, R> getCached, ScheduleListener<T, R> listener) {

		final R cached = getCached != null ? getCached.perform(parameter) : null;
		
		if (cached != null) {
			System.out.println("## return cached: " + forwardToCaller);
			forwardToCaller.forward(() -> listener.onScheduleResult(parameter, cached));
		}
		else {
		
			System.out.println("## add to queue");
			
			queue.add(parameter, debugName, (ScheduleFunction)performIO, (ScheduleListener)listener);
	
			System.out.println("## added to queue");
			
			if (thread == null) {
				this.thread = new Thread(() -> {
					
					for (;;) {
						try {
							threadMain();
						}
						catch (RuntimeException ex) {
							ex.printStackTrace();
						}
					}
				});
				
				thread.start();
			}
		}
	}
	
	private void threadMain() {
		// System.out.println("## retrieve from queue: " + queue);
		
		final ThreadIOQueue<Object, Object>.ScheduleState scheduleState = queue.take();
		
		// System.out.println("## got schedule state " + scheduleState.getDebugName());
		
		final Object result = scheduleState.getIOFunction().perform(scheduleState.getParameter());

		forwardToCaller.forward(() -> {

			for (ScheduleListener<Object, Object> resultListener : scheduleState.getListeners()) {
				
				// System.out.println("### forward to listener " + resultListener + " " + scheduleState.getDebugName());
				
				if (Thread.currentThread().getId() != scheduleState.getSchedulingThread()) {
					throw new IllegalStateException();
				}

				resultListener.onScheduleResult(scheduleState.getParameter(), result);
			}
		});
	}

}
