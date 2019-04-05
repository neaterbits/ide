package com.neaterbits.ide.util.dependencyresolution.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.util.dependencyresolution.executor.BuildEntity;
import com.neaterbits.ide.util.dependencyresolution.executor.Collectors;
import com.neaterbits.ide.util.dependencyresolution.executor.RecursiveBuildInfo;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.binary.logging.Loggable;

public final class Prerequisites extends BuildEntity implements Loggable {

	private static final String LOG_FIELD_PREREQUISITES = "prerequisitelist";
	
	private final int constructorLogSequenceNo; 
	private final List<Prerequisite<?>> prerequisites;
	private final String description;
	private final RecursiveBuildInfo<?, ?, ?> recursiveBuildInfo;
	private final Collectors<?> collectors;
	
	private Target<?> fromTarget;

	private static String getLogIdentifierValue() {
		return null;
	}

	private static String getLogLocalIdentifierValue() {
		return null;
	}

	public Prerequisites(
			LogContext logContext,
			List<Prerequisite<?>> prerequisites,
			String description,
			RecursiveBuildInfo<?, ?, ?> recursiveBuildInfo,
			Collectors<?> collectors) {
		
		final String identifier = getLogIdentifierValue();
		
		this.constructorLogSequenceNo = logConstructor(
				logContext,
				Prerequisites.class,
				identifier,
				getLogLocalIdentifierValue(),
				description);
		
		Objects.requireNonNull(prerequisites);
	
		final List<Prerequisite<?>> logged = logConstructorListField(logContext, LOG_FIELD_PREREQUISITES, prerequisites);
		
		this.prerequisites = logged != null ? Collections.unmodifiableList(logged) : null;
		this.description = description;
		this.recursiveBuildInfo = recursiveBuildInfo;
		this.collectors = collectors;

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
		return description;
	}

	@Override
	public String getDebugString() {
		return null;
	}

	@Override
	public BuildEntity getFromEntity() {
		return fromTarget;
	}

	public Target<?> getFromTarget() {
		return fromTarget;
	}

	void setFromTarget(Target<?> fromTarget) {
		
		Objects.requireNonNull(fromTarget);
		
		this.fromTarget = fromTarget;
	}

	public List<Prerequisite<?>> getPrerequisites() {
		return prerequisites;
	}

	public RecursiveBuildInfo<?, ?, ?> getRecursiveBuildInfo() {
		return recursiveBuildInfo;
	}

	public boolean isRecursiveBuild() {
		return recursiveBuildInfo != null;
	}

	public Collectors<?> getCollectors() {
		return collectors;
	}
	
	@Override
	public String toString() {
		return prerequisites.toString();
	}
}
