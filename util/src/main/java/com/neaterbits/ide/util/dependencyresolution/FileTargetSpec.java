package com.neaterbits.ide.util.dependencyresolution;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionFunction;
import com.neaterbits.ide.util.dependencyresolution.builder.ActionWithResultFunction;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.structuredlog.binary.logging.LogContext;

public final class FileTargetSpec<CONTEXT extends TaskContext, TARGET, FILE_TARGET>
			extends TargetSpec<CONTEXT, TARGET> {

	private final Class<FILE_TARGET> fileTargetType;
	private final BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget;
	private final Function<FILE_TARGET, File> file;

	public FileTargetSpec(
			Class<TARGET> type,
			Class<FILE_TARGET> fileTargetType,
			BiFunction<CONTEXT, TARGET, FILE_TARGET> getFileTarget,
			Function<FILE_TARGET, File> file,
			Function<TARGET, String> description,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites,
			Constraint constraint,
			ActionFunction<CONTEXT, TARGET> actionFunction,
			ActionWithResultFunction<CONTEXT, TARGET, ?> actionWithResult,
			ProcessResult<CONTEXT, TARGET, ?> onResult) {
		
		super(type, description, prerequisites, constraint, actionFunction, actionWithResult, onResult);
		
		this.fileTargetType = fileTargetType;
		this.getFileTarget = getFileTarget;
		this.file = file;
	}
	
	private FileTargetSpec(FileTargetSpec<CONTEXT, TARGET, FILE_TARGET> other, List<PrerequisiteSpec<CONTEXT, TARGET, ?>> additionalPrerequisites) {
		super(other, additionalPrerequisites);
		
		this.fileTargetType = other.fileTargetType;
		this.getFileTarget = other.getFileTarget;
		this.file = other.file;
	}
	
	@Override
	public TargetSpec<CONTEXT, TARGET> addPrerequisiteSpecs(List<PrerequisiteSpec<CONTEXT, TARGET, ?>> additionalPrerequisites) {

		return new FileTargetSpec<>(this, additionalPrerequisites);
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

	@Override
	Target<TARGET> createTarget(LogContext logContext, CONTEXT context, TARGET target, List<Prerequisites> prerequisitesList) {
		final FILE_TARGET fileTarget = getFileTarget.apply(context, target);
		
		return new FileTarget<>(
				logContext,
				getType(),
				file.apply(fileTarget),
				getDescription(target),
				target,
				prerequisitesList,
				makeAction(),
				makeActionWithResult(),
				this);
	}
}