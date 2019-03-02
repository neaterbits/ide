package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Map;
import java.util.Set;

public interface TargetBuildResult {

	Set<Target<?>> getCompletedTargets();

	Map<Target<?>, Exception> getFailedTargets();

}
