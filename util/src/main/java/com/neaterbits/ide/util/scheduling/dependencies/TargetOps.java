package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public interface TargetOps<CONTEXT extends TaskContext> {

	BaseTargetState<CONTEXT> onCheckPrerequisitesComplete(TargetExecutionContext<CONTEXT> context);
	
	BaseTargetState<CONTEXT> onActionPerformed(TargetExecutionContext<CONTEXT> context);

	BaseTargetState<CONTEXT> onActionError(TargetExecutionContext<CONTEXT> context, Exception ex);

	BaseTargetState<CONTEXT> onActionWithResultPerformed();
	
}
