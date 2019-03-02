package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;

final class InfoTarget<TARGET> extends Target<TARGET> {

	private final String name;

	InfoTarget(
			Class<TARGET> type,
			String name,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult,
			TargetSpec<?, TARGET, ?> targetSpec) {
		super(type, description, targetObject, prerequisites, action, actionWithResult, targetSpec);
		
		Objects.requireNonNull(name);
		
		if (targetObject != null) {
			throw new IllegalArgumentException(" " + targetObject + "/" + name);
		}
		

		if (targetSpec.getFile() != null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
	}

	@Override
	String getDebugString() {
		return name;
	}

	String getName() {
		return name;
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
		InfoTarget<?> other = (InfoTarget<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String targetSimpleLogString() {
		return name;
	}

	@Override
	public String targetToLogString() {
		return name;
	}

	@Override
	public String toString() {
		return "NamedTarget [name=" + name + ", description=" + getDescription() + "]";
	}
}
