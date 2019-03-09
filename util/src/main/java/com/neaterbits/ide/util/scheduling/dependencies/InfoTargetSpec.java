package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionFunction;
import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionWithResultFunction;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

public final class InfoTargetSpec<CONTEXT extends TaskContext, TARGET> extends TargetSpec<CONTEXT, TARGET>{
	
	private final String name;
	private final Function<TARGET, String> qualifierName;

	public InfoTargetSpec(
			Class<TARGET> type,
			String name,
			Function<TARGET, String> qualifierName,
			Function<TARGET, String> description,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisites,
			Constraint constraint,
			ActionFunction<CONTEXT, TARGET> actionFunction,
			ActionWithResultFunction<CONTEXT, TARGET, ?> actionWithResult,
			ProcessResult<CONTEXT, TARGET, ?> onResult) {
		
		super(type, description, prerequisites, constraint, actionFunction, actionWithResult, onResult);

		if (type == null && name == null) {
			throw new IllegalArgumentException("type == null && name == null");
		}

		this.name = name;
		this.qualifierName = qualifierName;
		
	}

	private InfoTargetSpec(InfoTargetSpec<CONTEXT, TARGET> other, List<PrerequisiteSpec<CONTEXT, TARGET, ?>> additionalPrerequisites) {

		super(other, additionalPrerequisites);
	
		this.name = other.name;
		this.qualifierName = other.qualifierName;
	}
	
	
	@Override
	public TargetSpec<CONTEXT, TARGET> addPrerequisiteSpecs(List<PrerequisiteSpec<CONTEXT, TARGET, ?>> additionalPrerequisites) {

		return new InfoTargetSpec<>(this, additionalPrerequisites);
	}
	
	String getName() {
		return name;
	}

	
	@Override
	Target<TARGET> createTarget(CONTEXT context, TARGET target, List<Prerequisites> prerequisitesList) {
		
		return new InfoTarget<>(
				getType(),
				name,
				qualifierName != null ? qualifierName.apply(target) : null,
				getDescription(target),
				target,
				prerequisitesList,
				makeAction(),
				makeActionWithResult(),
				this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final InfoTargetSpec<?, ?> other = (InfoTargetSpec<?, ?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
