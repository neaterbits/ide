package com.neaterbits.ide.util.scheduling.dependencies.builder;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisitesBuilderSpec;

public interface PrerequisitesOrActionBuilder<CONTEXT extends TaskContext, TARGET>
		extends PrerequisitesBuilder<CONTEXT, TARGET> {
	
	PrerequisitesOrActionBuilder<CONTEXT, TARGET> withPrerequisites(PrerequisitesBuilderSpec<CONTEXT, TARGET> buildSpec);

	<R> ResultProcessor<CONTEXT, TARGET, R> actionWithResult(Constraint constraint, ActionWithResultFunction<CONTEXT, TARGET, R> function);

	void action(Constraint constraint, ActionFunction<CONTEXT, TARGET> actionFunction);
}
