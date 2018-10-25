package com.neaterbits.ide.util.scheduling;

@FunctionalInterface
public interface ScheduleFunction<T, R> {

	R perform(T parameter);
	
}
