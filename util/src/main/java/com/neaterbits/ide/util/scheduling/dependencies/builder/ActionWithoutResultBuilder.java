package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.BiConsumer;

public interface ActionWithoutResultBuilder<CONTEXT extends TaskContext, T> {
	
	void ioBound(BiConsumer<CONTEXT, T> process);

	void cpuBound(BiConsumer<CONTEXT, T> process);

}
