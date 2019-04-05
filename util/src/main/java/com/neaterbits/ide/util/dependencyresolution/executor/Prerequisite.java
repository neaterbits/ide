package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.Objects;

import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

public final class Prerequisite<PREREQUISITE> extends BuildEntity implements Loggable {

	private static final String LOG_FIELD_SUBTARGET = "subTarget";

	private final int constructorLogSequenceNo;
	
	private final PREREQUISITE item;
	private final Target<PREREQUISITE> subTarget;
	
	private Prerequisites fromPrerequisites;
	
	public Prerequisite(LogContext logContext, PREREQUISITE item, Target<PREREQUISITE> subTarget) {
		
		this.constructorLogSequenceNo = logConstructor(logContext, getClass(), null, null, null);
		
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
	BuildEntity getFromEntity() {
		return fromPrerequisites;
	}

	Prerequisites getFromPrerequisites() {
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

	Target<PREREQUISITE> getSubTarget() {
		return subTarget;
	}

	@Override
	public String toString() {
		return item.toString();
	}
}
