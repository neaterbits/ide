package com.neaterbits.ide.util.dependencyresolution;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

final class Prerequisites extends BuildEntity implements Loggable {

	private static final String LOG_FIELD_PREREQUISITES = "prerequisitelist";
	
	private final int constructorLogSequenceNo; 
	private final List<Prerequisite<?>> prerequisites;
	private final PrerequisiteSpec<?, ?, ?> spec;

	private Target<?> fromTarget;

	private static String getLogIdentifierValue() {
		return null;
	}

	private static String getLogLocalIdentifierValue() {
		return null;
	}

	Prerequisites(LogContext logContext, List<Prerequisite<?>> prerequisites, PrerequisiteSpec<?, ?, ?> spec) {
		
		final String identifier = getLogIdentifierValue();
		
		this.constructorLogSequenceNo = logConstructor(logContext, Prerequisites.class, identifier, getLogLocalIdentifierValue(), spec.getDescription());
		
		Objects.requireNonNull(prerequisites);
		Objects.requireNonNull(spec);
	
		this.prerequisites = logConstructorListField(logContext, LOG_FIELD_PREREQUISITES, prerequisites);
		this.spec = spec;
		
		prerequisites.forEach(prerequisite -> prerequisite.setFromPrerequisites(this));
	}
	
	@Override
	public int getConstructorLogSequenceNo() {
		return constructorLogSequenceNo;
	}

	@Override
	public String getLogIdentifier() {
		return getLogIdentifierValue();
	}

	@Override
	public String getLogLocalIdentifier() {
		return getLogLocalIdentifierValue();
	}

	@Override
	public String getDescription() {
		return spec.getDescription();
	}

	@Override
	String getDebugString() {
		return null;
	}

	@Override
	BuildEntity getFromEntity() {
		return fromTarget;
	}

	Target<?> getFromTarget() {
		return fromTarget;
	}

	void setFromTarget(Target<?> fromTarget) {
		
		Objects.requireNonNull(fromTarget);
		
		this.fromTarget = fromTarget;
	}

	List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}
	
	PrerequisiteSpec<?, ?, ?> getSpec() {
		return spec;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	BiFunction<?, ?, Collection<?>> getPrerequisitesFunction() {
		return (BiFunction)spec.getPrerequisitesFunction();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	BiFunction<?, ?, Collection<?>> getSubPrerequisitesFunction() {
		return (BiFunction)spec.getSubPrerequisitesFunction();
	}

	Function<?, ?> getTargetFromSubPrerequisite() {
		return spec.getTargetFromPrerequisiteFunction();
	}

	boolean isRecursiveBuild() {
		return spec.isRecursiveBuild();
	}

	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
