package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;

final class TargetExecutionContext<CONTEXT> {
	final CONTEXT context;
	final ExecutorState state;
	final AsyncExecutor asyncExecutor;
	final TargetExecutorLogger logger;
	final Consumer<TargetBuildResult> onResult;
	
	TargetExecutionContext(CONTEXT context, ExecutorState state, AsyncExecutor asyncExecutor, TargetExecutorLogger logger, Consumer<TargetBuildResult> onResult) {
		this.context = context;
		this.state = state;
		this.asyncExecutor = asyncExecutor;
		this.logger = logger;
		this.onResult = onResult;
	}
}
