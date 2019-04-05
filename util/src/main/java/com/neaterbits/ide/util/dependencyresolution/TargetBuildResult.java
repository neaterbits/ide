package com.neaterbits.ide.util.dependencyresolution;

import java.util.Map;
import java.util.Set;

public interface TargetBuildResult {

	Set<Target<?>> getCompletedTargets();

	Map<Target<?>, Exception> getFailedTargets();

}
