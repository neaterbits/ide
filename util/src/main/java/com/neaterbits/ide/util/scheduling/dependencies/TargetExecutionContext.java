package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.function.Consumer;

final class TargetExecutionContext<CONTEXT> {
	final CONTEXT context;
	final TargetState state;
	final TargetExecutorLogger logger;
	final Consumer<TargetBuildResult> onResult;
	
	TargetExecutionContext(CONTEXT context, TargetState state, TargetExecutorLogger logger, Consumer<TargetBuildResult> onResult) {
		this.context = context;
		this.state = state;
		this.logger = logger;
		this.onResult = onResult;
	}
}
