package com.neaterbits.ide.util.scheduling;

import java.util.Objects;

final class SynchronousSchedulerImpl implements Scheduler {

	private final ForwardToCaller forwardToCaller;
	
	SynchronousSchedulerImpl(ForwardToCaller forwardToCaller) {
		
		Objects.requireNonNull(forwardToCaller);
		
		this.forwardToCaller = forwardToCaller;
	}

	@Override
	public <T, R> void schedule(
			Constraint constraint,
			T parameter,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener) {

		final R result = function.perform(parameter);

		forwardToCaller.forward(() -> {
			listener.onScheduleResult(parameter, result);
		});
	}
}
