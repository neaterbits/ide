package com.neaterbits.ide.util.dependencyresolution.model;

public interface TargetModelAccess {

	TargetKey<?> getRootTarget();
	
	<TARGET> TargetDefinition<TARGET> getTargetDefinition(TargetKey<TARGET> targetKey);
}
