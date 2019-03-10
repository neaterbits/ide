package com.neaterbits.ide.common.scheduling;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;
import com.neaterbits.ide.util.scheduling.ScheduleListener;

public class IDESchedulerImpl implements IDEScheduler {

	private final AsyncExecutor asyncExecutor;

	public IDESchedulerImpl(AsyncExecutor asyncExecutor) {

		Objects.requireNonNull(asyncExecutor);
		
		this.asyncExecutor = asyncExecutor;
	}

	@Override
	public <T, R> void scheduleTask(String name, String description, Constraint constraint,
			T param, ScheduleFunction<T, R> function, ScheduleListener<T, R> listener) {

		asyncExecutor.schedule(constraint, param, function, listener);
		
	}
}
