package com.neaterbits.ide.util.scheduling.dependencies.builder;

import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;

public final class TargetBuilderImpl<CONTEXT extends TaskContext> implements TargetBuilder<CONTEXT> {

	private NoTargetPrerequisitesBuilderImpl<CONTEXT> builder;
	
	@Override
	public NoTargetPrerequisitesBuilder<CONTEXT> addTarget(String targetName, String description) {
		
		if (this.builder != null) {
			throw new IllegalStateException();
		}
		
		this.builder = new NoTargetPrerequisitesBuilderImpl<>(targetName, description);
		
		return builder;
	}
	
	public TargetSpec<CONTEXT, ?, ?> build() {
		return builder.build();
	}
}
