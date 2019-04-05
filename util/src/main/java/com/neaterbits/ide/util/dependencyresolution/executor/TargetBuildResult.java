package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.Map;
import java.util.Set;

import com.neaterbits.ide.util.dependencyresolution.model.Target;

public interface TargetBuildResult {

	Set<Target<?>> getCompletedTargets();

	Map<Target<?>, Exception> getFailedTargets();

}
