package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.Objects;

import com.neaterbits.structuredlog.binary.logging.LogContext;

final class InfoTarget<TARGET> extends Target<TARGET> {

	private final String name;
	private final String qualifierName;

	private static String getLogIdentifier(String name, String qualifierName) {
		return name + (qualifierName != null ? "-" + qualifierName : "");
	}

	private static String getLogLocalIdentifier(String name) {
		return name;
	}

	InfoTarget(
			LogContext logContext,
			Class<TARGET> type,
			String name,
			String qualifierName,
			String description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult,
			InfoTargetSpec<?, TARGET> targetSpec) {
		super(
				logContext,
				getLogIdentifier(name, qualifierName),
				getLogLocalIdentifier(qualifierName),
				type,
				description,
				targetObject,
				prerequisites,
				action,
				actionWithResult,
				targetSpec);
		
		Objects.requireNonNull(name);
		
		this.name = name;
		this.qualifierName = qualifierName;
	}

	@Override
	public String getLogIdentifier() {
		return getLogIdentifier(name, qualifierName);
	}

	@Override
	public String getLogLocalIdentifier() {
		return getLogLocalIdentifier(name);
	}

	@Override
	String getDebugString() {
		return getLogIdentifier();
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
		return "InfoTarget [name=" + name + ", description=" + getDescription() + "]";
	}
}
