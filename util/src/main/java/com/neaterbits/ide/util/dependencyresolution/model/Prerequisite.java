package com.neaterbits.ide.util.dependencyresolution.model;

import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.executor.BuildEntity;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

public final class Prerequisite<PREREQUISITE> extends BuildEntity implements Loggable {

	private static final String LOG_FIELD_SUBTARGET = "subTarget";

	private final int constructorLogSequenceNo;
	
	private final PREREQUISITE item;
	private final TargetReference<PREREQUISITE> subTarget;
	
	private Prerequisites fromPrerequisites;

	public Prerequisite(LogContext logContext, PREREQUISITE item, TargetReference<PREREQUISITE> subTarget) {
		
		this.constructorLogSequenceNo = logConstructor(logContext, this, getClass(), null, null, null);
		
		Objects.requireNonNull(item);
		
		this.item = item;
		this.subTarget = logConstructorLoggableField(logContext, null, LOG_FIELD_SUBTARGET, subTarget);
		
		if (subTarget != null) {
			subTarget.setFromPrerequisite(this);
		}
	}

	@Override
	public int getConstructorLogSequenceNo() {
		return constructorLogSequenceNo;
	}

	@Override
	public String getLogIdentifier() {
		return null;
	}

	@Override
	public String getLogLocalIdentifier() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDebugString() {
		return null;
	}

	@Override
	public BuildEntity getFromEntity() {
		return fromPrerequisites;
	}

	public Prerequisites getFromPrerequisites() {
		return fromPrerequisites;
	}

	void setFromPrerequisites(Prerequisites fromPrerequisites) {

		if (this.fromPrerequisites != null) {
			throw new IllegalStateException();
		}
		
		this.fromPrerequisites = fromPrerequisites;
	}

	PREREQUISITE getItem() {
		return item;
	}

	public TargetReference<PREREQUISITE> getSubTarget() {
		return subTarget;
	}

	@Override
	public String toString() {
		return item.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
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
		Prerequisite<?> other = (Prerequisite<?>) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
}
