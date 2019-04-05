package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.ProcessResult;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface ResultProcessor<CONTEXT extends TaskContext, T, R> {

	void processResult(ProcessResult<CONTEXT, T, R> onResult);
}
