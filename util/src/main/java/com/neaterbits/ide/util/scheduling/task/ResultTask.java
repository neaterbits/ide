package com.neaterbits.ide.util.scheduling.task;

public abstract class ResultTask<T, R> extends Task<T> {

	abstract R perform(T data);
	
	ResultTask(T data) {
		super(data);
	}
}
