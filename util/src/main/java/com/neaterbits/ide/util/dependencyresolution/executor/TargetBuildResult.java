package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.Map;
import java.util.Set;

import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;

public interface TargetBuildResult {

	Set<TargetDefinition<?>> getCompletedTargets();

	Map<TargetDefinition<?>, Exception> getFailedTargets();

}
