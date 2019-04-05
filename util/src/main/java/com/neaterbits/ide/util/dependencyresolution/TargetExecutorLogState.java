package com.neaterbits.ide.util.dependencyresolution;

import java.util.Set;

interface TargetExecutorLogState extends TargetBuildResult {

	Set<Target<?>> getToExecuteTargets();

	Set<Target<?>> getScheduledTargets();
	
	Set<Target<?>> getActionPerformedCollectTargets();
	
	Status getTargetStatus(Target<?> target);
}
