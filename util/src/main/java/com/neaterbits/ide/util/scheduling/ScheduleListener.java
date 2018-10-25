package com.neaterbits.ide.util.scheduling;

@FunctionalInterface
public interface ScheduleListener<T, R> {

	void onScheduleResult(T parameter, R result);
	
}
