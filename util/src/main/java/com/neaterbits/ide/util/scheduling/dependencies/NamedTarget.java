package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;

final class NamedTarget<TARGET> extends Target<TARGET> {

	private final String name;

	NamedTarget(
			Class<TARGET> type,
			String name,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult) {
		super(type, description, targetObject, prerequisites, action, actionWithResult);
		
		Objects.requireNonNull(name);

		this.name = name;
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
		NamedTarget<?> other = (NamedTarget<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NamedTarget [name=" + name + ", description=" + getDescription() + "]";
	}
}
