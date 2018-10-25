package com.neaterbits.ide.util.scheduling.task;

public interface ResultProcessor<R> {

	void process(R result);
	
}
