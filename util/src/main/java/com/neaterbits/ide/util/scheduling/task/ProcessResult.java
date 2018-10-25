package com.neaterbits.ide.util.scheduling.task;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public interface ProcessResult<CONTEXT extends TaskContext, T, R> {

	void process(CONTEXT context, T t, R r);

}
