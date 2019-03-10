package com.neaterbits.ide.common.scheduling;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.ScheduleFunction;
import com.neaterbits.ide.util.scheduling.ScheduleListener;

public interface IDEScheduler {

	<T, R>void scheduleTask(
			String name,
			String description,
			Constraint constraint,
			T param,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener);
	
}
