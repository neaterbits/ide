package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Objects;

final class Prerequisite<PREREQUISITE> {

	private final PREREQUISITE item;
	private final Target<PREREQUISITE> subTarget;

	public Prerequisite(PREREQUISITE item, Target<PREREQUISITE> subTarget) {
		
		Objects.requireNonNull(item);
		
		this.item = item;
		this.subTarget = subTarget;
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
