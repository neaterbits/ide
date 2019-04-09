package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.Set;

import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;

public interface TargetExecutorLogState extends TargetBuildResult {

	Set<TargetDefinition<?>> getToExecuteTargets();

	Set<TargetDefinition<?>> getScheduledTargets();
	
	Set<TargetDefinition<?>> getActionPerformedCollectTargets();

	Status getTargetStatus(TargetDefinition<?> target);
}
