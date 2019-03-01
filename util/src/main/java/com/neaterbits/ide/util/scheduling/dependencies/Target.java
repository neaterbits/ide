package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.util.Indent;

public abstract class Target<TARGET> {

	private final Class<TARGET> type;
	private final String description;
	private final TARGET targetObject;
	private final List<Prerequisites> prerequisites;
	private final Action<TARGET> action;
	private final ActionWithResult<TARGET> actionWithResult;
	private final TargetSpec<?, TARGET, ?> targetSpec;

	private Prerequisite<?> fromPrerequisite;
	
	public abstract String targetSimpleLogString();

	public abstract String targetToLogString();
	
	Target(
			Class<TARGET> type,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult,
			TargetSpec<?, TARGET, ?> targetSpec) {
		
		Objects.requireNonNull(targetSpec);
		
		this.type = type;
		this.description = description;
		this.targetObject = targetObject;
		this.prerequisites = prerequisites;
		this.action = action;
		this.actionWithResult = actionWithResult;
		this.targetSpec = targetSpec;
	}

	final Class<TARGET> getType() {
		return type;
	}

	Prerequisite<?> getFromPrerequisite() {
		return fromPrerequisite;
	}

	void setFromPrerequisite(Prerequisite<?> fromPrerequisite) {
		
		if (this.fromPrerequisite != null) {
			throw new IllegalStateException();
		}
		
		this.fromPrerequisite = fromPrerequisite;
	}

	final String getDescription() {
		return description;
	}

	final TARGET getTargetObject() {
		return targetObject;
	}

	final List<Prerequisites> getPrerequisites() {
		return prerequisites;
	}

	final Action<TARGET> getAction() {
		return action;
	}

	final ActionWithResult<TARGET> getActionWithResult() {
		return actionWithResult;
	}

	TargetSpec<?, TARGET, ?> getTargetSpec() {
		return targetSpec;
	}

	public final void printTargets() {
		printTargets(0);
	}
	
	private void printTargets(int indent) {
		System.out.println(Indent.indent(indent) + "Target " + this + ", prerequisites " + getPrerequisites() + ", action " + action);
		
		for (Prerequisites prerequisites : getPrerequisites()) {
			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				if (prerequisite.getSubTarget() != null) {
					prerequisite.getSubTarget().printTargets(indent + 1);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetObject == null) ? 0 : targetObject.hashCode());
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
		Target<?> other = (Target<?>) obj;
		if (targetObject == null) {
			if (other.targetObject != null)
				return false;
		} else if (!targetObject.equals(other.targetObject))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}

