package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.io.File;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

class PrerequisitesBuilderImpl<CONTEXT extends TaskContext, TARGET, FILE_TARGET>
		implements PrerequisitesBuilder<CONTEXT, TARGET> {
	
	private final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState;
	
	PrerequisitesBuilderImpl(Class<TARGET> type, String targetName, Function<TARGET, String> description) {

		this.targetBuilderState = new TargetBuilderState<>(type, targetName, description);
	}

	PrerequisitesBuilderImpl(Class<TARGET> type, Class<FILE_TARGET> fileTargetType, BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget, Function<FILE_TARGET, File> file, Function<TARGET, String> description) {

		this.targetBuilderState = new TargetBuilderState<>(type, fileTargetType, getFileTarget, file, description);
	}
	
	PrerequisitesBuilderImpl(TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> targetBuilderState) {

		Objects.requireNonNull(targetBuilderState);
		
		this.targetBuilderState = targetBuilderState;
	}

	@Override
	public final TargetPrerequisitesBuilder<CONTEXT, TARGET> prerequisites(String description) {
		return new TargetPrerequisitesBuilderImpl<>(targetBuilderState, description);
	}

	@Override
	public final TargetPrerequisiteBuilder<CONTEXT, TARGET> prerequisite(String description) {
		return new TargetPrerequisiteBuilderImpl<>(targetBuilderState, description);
	}

	final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> getTargetBuilderState() {
		return targetBuilderState;
	}

	final TargetBuilderState<CONTEXT, TARGET, FILE_TARGET> build() {
		return targetBuilderState;
	}
}
