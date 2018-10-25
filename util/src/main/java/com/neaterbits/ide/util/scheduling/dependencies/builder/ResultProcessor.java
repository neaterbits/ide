package com.neaterbits.ide.util.scheduling.dependencies.builder;

import com.neaterbits.ide.util.scheduling.task.ProcessResult;

public interface ResultProcessor<CONTEXT extends TaskContext, T, R> {

	void processResult(ProcessResult<CONTEXT, T, R> onResult);
}
