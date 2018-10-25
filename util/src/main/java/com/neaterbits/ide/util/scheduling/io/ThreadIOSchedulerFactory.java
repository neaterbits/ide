package com.neaterbits.ide.util.scheduling.io;

import com.neaterbits.ide.util.scheduling.ForwardToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;

public class ThreadIOSchedulerFactory implements IOSchedulerFactory {

	@Override
	public <T, R> IOScheduler<T, R> makeScheduler(
			ScheduleFunction<T, R> ioFunction,
			ScheduleFunction<T, R> getCached,
			ForwardToCaller forwardToCaller) {

		return new ThreadIOScheduler<>(ioFunction, getCached, forwardToCaller);
	}
}
