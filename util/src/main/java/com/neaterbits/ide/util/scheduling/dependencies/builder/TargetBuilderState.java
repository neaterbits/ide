package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisiteSpec;
import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

final class TargetBuilderState<CONTEXT extends TaskContext, TARGET, FILE_TARGET> {

	private final Class<TARGET> targetType;
	
	private final String targetName;

	private final BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget;
	private final Class<FILE_TARGET> fileTargetType;
	private final Function<FILE_TARGET, File> file;
	private final Function<TARGET, String> qualifierName;
	private final Function<TARGET, String> description;
	
	private final List<PrerequisiteBuilderState<CONTEXT, TARGET, ?, ?>> prerequisites;
	
	private Constraint constraint;
	
	private ActionFunction<CONTEXT, TARGET> actionFunction;

	private BiFunction<CONTEXT, TARGET, ?> actionWithResult;
	private ProcessResult<CONTEXT, TARGET, ?> onResult;
	
	TargetBuilderState(Class<TARGET> type, String targetName, Function<TARGET, String> qualifierName, Function<TARGET, String> description) {
		this.targetType = type;
		
		this.targetName = targetName;
		this.qualifierName = qualifierName;
		
		this.getFileTarget = null;
		this.fileTargetType = null;
		this.file = null;
		
		this.description = description;
		this.prerequisites = new ArrayList<>();
	}
	
	TargetBuilderState(Class<TARGET> type, Class<FILE_TARGET> fileTargetType, BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget, Function<FILE_TARGET, File> file, Function<TARGET, String> description) {
		this.targetType = type;
		
		this.targetName = null;
		this.qualifierName = null;
		
		this.getFileTarget = getFileTarget;
		this.fileTargetType = fileTargetType;
		this.file = file;
		
		this.description = description;
		this.prerequisites = new ArrayList<>();
	}
	
	final Class<TARGET> getType() {
		return targetType;
	}

	final String getTargetName() {
		return targetName;
	}
	
	final FILE_TARGET getFileTarget(CONTEXT context, TARGET target) {
		return getFileTarget.apply(context, target);
	}

	final Class<FILE_TARGET> getFileTargetType() {
		return fileTargetType;
	}

	final Function<FILE_TARGET, File> getFile() {
		return file;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final String getDescription(Object target) {
		return (String)((Function)description).apply(target);
	}

	final void addPrerequisiteBuilder(PrerequisiteBuilderState<CONTEXT, TARGET, ?, ?> builder) {
		
		Objects.requireNonNull(builder);

		prerequisites.add(builder);
	}
	
	final List<PrerequisiteBuilderState<CONTEXT, TARGET, ?, ?>> getPrerequisites() {
		return prerequisites;
	}
	
	final void setAction(Constraint constraint, ActionFunction<CONTEXT, TARGET> actionFunction) {
		
		Objects.requireNonNull(constraint);
		Objects.requireNonNull(actionFunction);

		this.constraint = constraint;
		this.actionFunction = actionFunction;
	}

	final void setActionWithResult(Constraint constraint, BiFunction<CONTEXT, TARGET, ?> actionWithResult) {
		
		Objects.requireNonNull(constraint);
		Objects.requireNonNull(actionWithResult);

		this.constraint = constraint;
		this.actionWithResult = actionWithResult;
	}
	
	final void setOnResult(ProcessResult<CONTEXT, TARGET, ?> onResult) {
		Objects.requireNonNull(onResult);
	
		this.onResult = onResult;
	}
	

	final TargetSpec<CONTEXT, TARGET, FILE_TARGET> build() {
		
		final List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites = new ArrayList<>(this.prerequisites.size());
		
		for (PrerequisiteBuilderState<CONTEXT, TARGET, ?, ?> prerequisite : this.prerequisites) {
			prerequisites.add(prerequisite.build());
		}
		
		return targetName != null
				? new TargetSpec<>(targetType, targetName, qualifierName, description, prerequisites, constraint, actionFunction, actionWithResult, onResult)
				: new TargetSpec<>(targetType, fileTargetType, getFileTarget, file, description, prerequisites, constraint, actionFunction, actionWithResult, onResult);
	}
}
