package com.neaterbits.ide.util.scheduling.dependencies;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionFunction;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

public final class TargetSpec<CONTEXT extends TaskContext, TARGET, FILE_TARGET> {

	private final Class<TARGET> type;

	private final String name;

	private final Class<FILE_TARGET> fileTargetType;
	private final BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget;
	private final Function<FILE_TARGET, File> file;

	private final Function<TARGET, String> description;
	
	private final List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites;

	private final Constraint constraint;
	private final ActionFunction<CONTEXT, TARGET> actionFunction;
	
	private BiFunction<CONTEXT, TARGET, ?> actionWithResult;
	private ProcessResult<CONTEXT, TARGET, ?> onResult;

	private TargetSpec(
			Class<TARGET> type,
			String name,
			Class<FILE_TARGET> fileTargetType,
			BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget,
			Function<FILE_TARGET, File> file,
			Function<TARGET, String> description,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites,
			Constraint constraint,
			ActionFunction<CONTEXT, TARGET> actionFunction,
			BiFunction<CONTEXT, TARGET, ?> actionWithResult,
			ProcessResult<CONTEXT, TARGET, ?> onResult) {
		
		if (type == null && name == null) {
			throw new IllegalArgumentException("type == null && name == null");
		}
		
		this.type = type;
		this.name = name;
		
		this.fileTargetType = fileTargetType;
		this.getFileTarget = getFileTarget;
		this.file = file;
		this.description = description;
		this.prerequisites = prerequisites != null ? Collections.unmodifiableList(prerequisites) : null;
		
		this.constraint = constraint;
		this.actionFunction = actionFunction;

		this.actionWithResult = actionWithResult;
		this.onResult = onResult;
	}

	public TargetSpec(
			Class<TARGET> type,
			String name,
			Function<TARGET, String> description,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites,
			Constraint constraint,
			ActionFunction<CONTEXT, TARGET> actionFunction,
			BiFunction<CONTEXT, TARGET, ?> actionWithResult,
			ProcessResult<CONTEXT, TARGET, ?> onResult) {
		this(type, name, null, null, null, description, prerequisites, constraint, actionFunction, actionWithResult, onResult);
	}

	public TargetSpec(
			Class<TARGET> type,
			Class<FILE_TARGET> fileTargetType,
			BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget,
			Function<FILE_TARGET, File> file,
			Function<TARGET, String> description,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites,
			Constraint constraint,
			ActionFunction<CONTEXT, TARGET> actionFunction,
			BiFunction<CONTEXT, TARGET, ?> actionWithResult,
			ProcessResult<CONTEXT, TARGET, ?> onResult) {
		this(type, null, fileTargetType, getFileTarget, file, description, prerequisites, constraint, actionFunction, actionWithResult, onResult);
	}

	Class<TARGET> getType() {
		return type;
	}

	String getName() {
		return name;
	}
	
	Class<FILE_TARGET> getFileTargetType() {
		return fileTargetType;
	}

	BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget() {
		return getFileTarget;
	}

	Function<FILE_TARGET, File> getFile() {
		return file;
	}

	List<PrerequisiteSpec<CONTEXT, TARGET, ?>> getPrerequisiteSpecs() {
		return prerequisites;
	}

	Action<TARGET> makeAction() {
		return actionFunction != null
				? new Action<>(constraint, actionFunction)
				: null;
	}

	ActionWithResult<TARGET> makeActionWithResult() {
		return actionWithResult != null
				? new ActionWithResult<>(constraint, actionWithResult, onResult)
				: null;
	}

	Target<TARGET> createTarget(CONTEXT context, TARGET target, List<Prerequisites> prerequisitesList) {

		final Target<TARGET> createdTarget;
		
		final String description = this.description.apply(target);
		
		if (name != null) {
			createdTarget = new NamedTarget<>(
					type,
					name,
					description,
					target,
					prerequisitesList,
					makeAction(),
					makeActionWithResult(),
					this);
		}
		else if (file != null) {
			
			final FILE_TARGET fileTarget = getFileTarget.apply(context, target);
			
			createdTarget = new FileTarget<>(
					type,
					file.apply(fileTarget),
					description,
					target,
					prerequisitesList,
					makeAction(),
					makeActionWithResult(),
					this);
		}
		else {
			throw new UnsupportedOperationException();
		}

		return createdTarget;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TargetSpec<?, ?, ?> other = (TargetSpec<?, ?, ?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
