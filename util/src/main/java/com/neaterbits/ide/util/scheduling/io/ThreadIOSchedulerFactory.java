package com.neaterbits.ide.util.scheduling.io;

import com.neaterbits.ide.util.scheduling.ForwardResultToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;

public class ThreadIOSchedulerFactory implements IOSchedulerFactory {

	@Override
	public <T, R> IOScheduler<T, R> makeScheduler(
			ScheduleFunction<T, R> ioFunction,
			ScheduleFunction<T, R> getCached,
			ForwardResultToCaller forwardToCaller) {

		return new ThreadIOScheduler<>(ioFunction, getCached, forwardToCaller);
	}
}
