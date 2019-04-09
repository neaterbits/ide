package com.neaterbits.ide.util.dependencyresolution.model;

import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

public class TargetReference<TARGET> extends TargetKey<TARGET> implements Loggable {

	private final int constructorSequenceNo;
	
	private final Function<TARGET, String> description;
	
	private TargetDefinition<TARGET> targetDefinition;
	
	private Prerequisite<?> fromPrerequisite;
	
	public TargetReference(
			LogContext logContext,
			Class<TARGET> type,
			TARGET targetObject,
			Function<TARGET, String> description) {
		
		super(type, targetObject);
	
		this.constructorSequenceNo = logConstructor(logContext, this, TargetReference.class, null, null, description != null ? description.apply(targetObject) : null);
		this.description = description;
	}
	
	@Override
	public int getConstructorLogSequenceNo() {
		return constructorSequenceNo;
	}
	
	@Override
	public String getLogIdentifier() {
		return null;
	}

	@Override
	public String getLogLocalIdentifier() {
		return null;
	}

	public final boolean isRoot() {
		return fromPrerequisite == null;
	}
	
	public final Prerequisite<?> getFromPrerequisite() {
		return fromPrerequisite;
	}

	public final TargetDefinition<TARGET> getTargetDefinitionIfAny() {
		return targetDefinition;
	}
	
	final void setTargetDefinition(TargetDefinition<TARGET> targetDefinition) {
		
		Objects.requireNonNull(targetDefinition);
		
		this.targetDefinition = targetDefinition;
	}

	void setFromPrerequisite(Prerequisite<?> fromPrerequisite) {
		
		if (this.fromPrerequisite != null) {
			throw new IllegalStateException();
		}
		
		this.fromPrerequisite = fromPrerequisite;
	}

	private TargetReference<?> getFromTarget() {
		return fromPrerequisite != null
				? fromPrerequisite.getFromPrerequisites().getFromTarget()
				: null;
	}
	
	public final boolean isRecursionSubTarget() {
		return fromPrerequisite != null ? fromPrerequisite.getFromPrerequisites().isRecursiveBuild() : false;
	}

	public final String getDescription() {
		return description != null ? description.apply(getTargetObject()) : null;
	}

	final Function<TARGET, String> getDescriptionFunction() {
		return description;
	}
	
	public final TargetReference<?> getTopOfRecursion() {

		if (!isRecursionSubTarget()) {
			throw new IllegalStateException();
		}
		
		TargetReference<?> result = null;
		
		for (TargetReference<?> target = this;
				   target != null
				&& target.isRecursionSubTarget();
				target = target.getFromTarget()) {
			
			result = target;
		}
		
		if (!result.isTopOfRecursion()) {
			throw new IllegalStateException();
		}

		return result;
	}
	
	public final boolean isTopOfRecursion() {
		
		return getRecursionLevel() == 0;
	}
	
	public final int getRecursionLevel() {
		
		int level = 0;
		
		if (!isRecursionSubTarget()) {
			throw new IllegalStateException("Not a recursion target " + this);
		}
		
		for (TargetReference<?> target = getFromTarget();
				   target != null
				&& target.isRecursionSubTarget()
				// && target.targetSpec == this.targetSpec
				;
				
				target = target.getFromTarget()) {
			
			++ level;
		}
		
		return level;
	}

	@Override
	public String toString() {
		return "TargetReference [description=" + getDescription() + ", toString()=" + super.toString() + "]";
	}
}