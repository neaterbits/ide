package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.BiFunction;

public interface ActionWithResultBuilder<CONTEXT extends TaskContext, T> {

	<R> ResultProcessor<CONTEXT, T, R> ioBound(BiFunction<CONTEXT, T, R> process);
	
	<R> ResultProcessor<CONTEXT, T, R> cpuBound(BiFunction<CONTEXT, T, R> process);

}
