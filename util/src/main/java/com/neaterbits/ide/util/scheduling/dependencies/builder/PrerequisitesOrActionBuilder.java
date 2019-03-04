package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisitesBuildSpec;

public interface PrerequisitesOrActionBuilder<CONTEXT extends TaskContext, TARGET>
		extends PrerequisitesBuilder<CONTEXT, TARGET> {
	
	PrerequisitesOrActionBuilder<CONTEXT, TARGET> withPrerequisites(PrerequisitesBuildSpec<CONTEXT, TARGET> buildSpec);

	<R> ResultProcessor<CONTEXT, TARGET, R> actionWithResult(Constraint constraint, BiFunction<CONTEXT, TARGET, R> function);

	void action(Constraint constraint, ActionFunction<CONTEXT, TARGET> actionFunction);
}
