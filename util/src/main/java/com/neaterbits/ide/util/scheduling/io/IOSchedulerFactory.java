package com.neaterbits.ide.util.scheduling.io;

import com.neaterbits.ide.util.scheduling.ForwardResultToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;

public interface IOSchedulerFactory {

	<T, R> IOScheduler<T, R> makeScheduler(
			ScheduleFunction<T, R> ioFunction,
			ScheduleFunction<T, R> getCached,
			ForwardResultToCaller forwardToCaller);
	
}
