package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

final class TargetExecutionContext<CONTEXT extends TaskContext> {
	final CONTEXT context;
	final ExecutorState<CONTEXT> state;
	final AsyncExecutor asyncExecutor;
	final TargetExecutorLogger logger;
	final Consumer<TargetBuildResult> onResult;
	
	TargetExecutionContext(CONTEXT context, ExecutorState<CONTEXT> state, AsyncExecutor asyncExecutor, TargetExecutorLogger logger, Consumer<TargetBuildResult> onResult) {
		this.context = context;
		this.state = state;
		this.asyncExecutor = asyncExecutor;
		this.logger = logger;
		this.onResult = onResult;
	}
}
