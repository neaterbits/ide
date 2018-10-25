package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;

public interface PrerequisitesOrActionBuilder<CONTEXT extends TaskContext, TARGET>
		extends PrerequisitesBuilder<CONTEXT, TARGET> {
	
	<R> ResultProcessor<CONTEXT, TARGET, R> actionWithResult(Constraint constraint, BiFunction<CONTEXT, TARGET, R> function);

	void action(Constraint constraint, ActionFunction<CONTEXT, TARGET> actionFunction);
}
