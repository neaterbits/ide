package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.Set;

import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.executor.Target;
import com.neaterbits.ide.util.dependencyresolution.executor.TargetBuildResult;

public interface TargetExecutorLogState extends TargetBuildResult {

	Set<Target<?>> getToExecuteTargets();

	Set<Target<?>> getScheduledTargets();
	
	Set<Target<?>> getActionPerformedCollectTargets();
	
	Status getTargetStatus(Target<?> target);
}
