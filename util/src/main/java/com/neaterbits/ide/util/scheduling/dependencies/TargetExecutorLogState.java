package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Map;
import java.util.Set;

interface TargetExecutorLogState extends TargetBuildResult {

	Set<Target<?>> getToExecuteTargets();

	Set<Target<?>> getScheduledTargets();
	
	Map<Target<?>, Object> getCollected();
	Map<Class<?>, Object> getPrerequisites();

}
