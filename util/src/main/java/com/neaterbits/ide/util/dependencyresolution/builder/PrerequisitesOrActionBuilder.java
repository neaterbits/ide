package com.neaterbits.ide.util.dependencyresolution.builder;

import com.neaterbits.ide.util.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisitesOrActionBuilder<CONTEXT extends TaskContext, TARGET>
		extends PrerequisitesBuilder<CONTEXT, TARGET> {
	
	PrerequisitesOrActionBuilder<CONTEXT, TARGET> withPrerequisites(PrerequisitesBuilderSpec<CONTEXT, TARGET> buildSpec);

	<R> ResultProcessor<CONTEXT, TARGET, R> actionWithResult(Constraint constraint, ActionWithResultFunction<CONTEXT, TARGET, R> function);

	void action(Constraint constraint, ActionFunction<CONTEXT, TARGET> actionFunction);
}
