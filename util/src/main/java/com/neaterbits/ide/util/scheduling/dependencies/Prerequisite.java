package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Objects;

final class Prerequisite<PREREQUISITE> extends BuildEntity {

	private final PREREQUISITE item;
	private final Target<PREREQUISITE> subTarget;
	
	private Prerequisites fromPrerequisites;
	
	Prerequisite(PREREQUISITE item, Target<PREREQUISITE> subTarget) {
		
		Objects.requireNonNull(item);
		
		this.item = item;
		this.subTarget = subTarget;
		
		if (subTarget != null) {
			subTarget.setFromPrerequisite(this);
		}
	}

	@Override
	String getDebugString() {
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
