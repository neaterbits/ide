package com.neaterbits.ide.util.dependencyresolution.executor;


import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionFunction;
import com.neaterbits.ide.util.scheduling.Constraint;

public final class Action<TARGET> {

	private final Constraint constraint;
	private final ActionFunction<?, TARGET> actionFunction;

	public Action(Constraint constraint, ActionFunction<?, TARGET> actionFunction) {
		this.constraint = constraint;
		this.actionFunction = actionFunction;
	}

	Constraint getConstraint() {
		return constraint;
	}

	ActionFunction<?, TARGET> getActionFunction() {
		return actionFunction;
	}

	@Override
	public String toString() {
		return "Action [constraint=" + constraint + ", actionFunction=" + actionFunction + "]";
	}
}
