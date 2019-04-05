package com.neaterbits.ide.util.dependencyresolution.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.ide.util.dependencyresolution.executor.Action;
import com.neaterbits.ide.util.dependencyresolution.executor.ActionWithResult;
import com.neaterbits.ide.util.dependencyresolution.executor.BuildEntity;
import com.neaterbits.ide.util.dependencyresolution.executor.RecursiveBuildInfo;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

public abstract class Target<TARGET> extends BuildEntity implements Loggable {

	private static final String LOG_FIELD_PREREQUISITES = "prerequisites";
	
	private final LogContext logContext;
	private final int constructorLogSequenceNo;
	private final Class<TARGET> type;
	private final Function<TARGET, String> description;
	private final TARGET targetObject;
	private List<Prerequisites> prerequisites;
	private final Action<TARGET> action;
	private final ActionWithResult<TARGET> actionWithResult;

	private Prerequisite<?> fromPrerequisite;
	
	public abstract String targetSimpleLogString();

	public abstract String targetToLogString();

	public abstract <CONTEXT> Target<TARGET> createTarget(
			LogContext logContext,
			CONTEXT context,
			TARGET target,
			List<Prerequisites> prerequisitesList);

	
	Target(
			LogContext logContext,
			String logIdentifier,
			String logLocalIdentifier,
			Class<TARGET> type,
			Function<TARGET, String> description,
			TARGET targetObject,
			List<Prerequisites> prerequisites,
			Action<TARGET> action,
			ActionWithResult<TARGET> actionWithResult) {
		
		this.constructorLogSequenceNo = logConstructor(
				logContext,
				Target.class,
				logIdentifier,
				logLocalIdentifier,
				description.apply(targetObject));
		
		this.logContext = logContext;
		this.type = type;
		this.description = description;
		this.targetObject = targetObject;
		this.prerequisites = logConstructorListField(logContext, LOG_FIELD_PREREQUISITES, prerequisites);
		this.action = action;
		this.actionWithResult = actionWithResult;
		
		updatePrerequisites(prerequisites);
	}
	
	@Override
	public final int getConstructorLogSequenceNo() {
		return constructorLogSequenceNo;
	}

	public final LogContext getLogContext() {
		return logContext;
	}

	final Class<TARGET> getType() {
		return type;
	}

	public final boolean isRoot() {
		return fromPrerequisite == null;
	}
	
	@Override
	public final BuildEntity getFromEntity() {
		return fromPrerequisite;
	}

	public final Prerequisite<?> getFromPrerequisite() {
		return fromPrerequisite;
	}

	void setFromPrerequisite(Prerequisite<?> fromPrerequisite) {
		
		if (this.fromPrerequisite != null) {
			throw new IllegalStateException();
		}
		
		this.fromPrerequisite = fromPrerequisite;
	}

	@Override
	public final String getDescription() {
		return description.apply(targetObject);
	}
	
	final Function<TARGET, String> getDescriptionFunction() {
		return description;
	}

	public final TARGET getTargetObject() {
		return targetObject;
	}

	public final List<Prerequisites> getPrerequisites() {
		return prerequisites;
	}

	public void updatePrerequisites(List<Prerequisites> prerequisites) {

		prerequisites.forEach(p -> p.setFromTarget(this));

		this.prerequisites = Collections.unmodifiableList(prerequisites);
	}

	public final Action<TARGET> getAction() {
		return action;
	}

	public final ActionWithResult<TARGET> getActionWithResult() {
		return actionWithResult;
	}

	public final void printTargets() {
		printTargets(0);
	}
	
	private Target<?> getFromTarget() {
		return fromPrerequisite != null
				? fromPrerequisite.getFromPrerequisites().getFromTarget()
				: null;
	}
	
	public final boolean isRecursionSubTarget() {
		return fromPrerequisite != null ? fromPrerequisite.getFromPrerequisites().isRecursiveBuild() : false;
	}

	
	public final boolean isTopOfRecursion() {
		
		return getRecursionLevel() == 0;
	}
	
	public final int getRecursionLevel() {
		
		int level = 0;
		
		for (Target<?> target = this;
				   target != null
				&& target.isRecursionSubTarget()
				// && target.targetSpec == this.targetSpec
				;
				
				target = target.getFromTarget()) {
			
			++ level;
		}
		
		return level;
	}
	
	public static Target<?> findRecursionTop(Prerequisites prerequisites) {

		if (!prerequisites.isRecursiveBuild()) {
			throw new IllegalArgumentException();
		}
		
		RecursiveBuildInfo<?, ?, ?> recursiveBuildInfo = prerequisites.getRecursiveBuildInfo();
		Prerequisites prq;

		Target<?> initialTarget = null;

		for (
				prq = prerequisites;
				prq.getRecursiveBuildInfo() == recursiveBuildInfo && prq.getFromTarget() != null;
				prq = prq.getFromTarget().getFromPrerequisite().getFromPrerequisites()) {
			
			/*
			System.out.println("## target " + prq.getFromTarget() + "/"
						+ prq.getFromTarget().getFromPrerequisite().getFromPrerequisites().isRecursiveBuild());
			
			*/
			initialTarget = prq.getFromTarget();
		}

		if (!initialTarget.isTopOfRecursion()) {
			throw new IllegalStateException();
		}
		
		return initialTarget;
	}
	
	private void printTargets(int indent) {

		// System.out.println(Indent.indent(indent) + "Target " + this + ", prerequisites " + getPrerequisites() + ", action " + action);
		
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

