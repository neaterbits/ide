package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Set;

interface TargetExecutorLogState extends TargetBuildResult {

	Set<Target<?>> getToExecuteTargets();

	Set<Target<?>> getScheduledTargets();
	
	Set<Target<?>> getActionPerformedCollectTargets();
	
	Status getTargetStatus(Target<?> target);
}
