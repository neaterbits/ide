package com.neaterbits.ide.util.scheduling.io;

import com.neaterbits.ide.util.scheduling.ForwardToCaller;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;

public interface IOSchedulerFactory {

	<T, R> IOScheduler<T, R> makeScheduler(
			ScheduleFunction<T, R> ioFunction,
			ScheduleFunction<T, R> getCached,
			ForwardToCaller forwardToCaller);
	
}
