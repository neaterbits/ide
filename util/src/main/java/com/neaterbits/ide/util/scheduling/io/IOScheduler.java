package com.neaterbits.ide.util.scheduling.io;

import com.neaterbits.ide.util.scheduling.ScheduleListener;

public interface IOScheduler<T, R> {

	void schedule(T parameter, String debugName, ScheduleListener<T, R> listener);
	
}
