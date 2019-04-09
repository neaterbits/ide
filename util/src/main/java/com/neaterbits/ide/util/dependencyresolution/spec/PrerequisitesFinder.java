package com.neaterbits.ide.util.dependencyresolution.spec;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.TargetReference;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.structuredlog.binary.logging.LogContext;

abstract class PrerequisitesFinder {

	final AsyncExecutor asyncExecutor;
	
	PrerequisitesFinder(AsyncExecutor asyncExecutor) {
		
		Objects.requireNonNull(asyncExecutor);

		this.asyncExecutor = asyncExecutor;
	}

	abstract <CONTEXT extends TaskContext, TARGET>
	void findTargets(
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET> targetSpec,
			LogContext logContext,
			CONTEXT context,
			TARGET target,
			TargetFinderLogger logger,
			int indent,
			Consumer<TargetReference<TARGET>> targetCreated);
	
	final <CONTEXT extends TaskContext, TARGET, PREREQUISITE>
	void getPrerequisites(
			LogContext logContext,
			CONTEXT context,
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			TargetFinderLogger logger,
			int indent,
			Consumer<Set<Prerequisite<?>>> listener) {
		
		if (prerequisiteSpec.getConstraint() != null) {

			asyncExecutor.schedule(
					prerequisiteSpec.getConstraint(),
					null,
					param -> {
						return prerequisiteSpec.getPrerequisites(context, target);
					},
					(param, result) -> {

						getPrerequisites(logContext, context, fromPrerequisites, targetSpec, target, prerequisiteSpec, result, logger, indent, prerequisites -> {
						
							if (logger != null) {
								logger.onPrerequisites(indent, targetSpec, target, prerequisiteSpec, prerequisites);
							}
														
							listener.accept(prerequisites);
						});
					});
		} else {
			final Collection<PREREQUISITE> sub = prerequisiteSpec.getPrerequisites(context, target);

			getPrerequisites(logContext, context, fromPrerequisites, targetSpec, target, prerequisiteSpec, sub, logger, indent, prerequisites -> {
				
				if (logger != null) {
					logger.onPrerequisites(indent, targetSpec, target, prerequisiteSpec, prerequisites);
				}
				
				listener.accept(prerequisites);
			});
		}
	}

	private <CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE>
	void getPrerequisites(
			LogContext logContext,
			CONTEXT context,
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			Collection<PREREQUISITE> sub,
			TargetFinderLogger logger,
			int indent,
			Consumer<Set<Prerequisite<?>>> listener) {

		if (sub == null) {
			throw new IllegalStateException("No prerequisites for " + targetSpec.getType().getSimpleName() + "/" + target + "/" + prerequisiteSpec.getDescription());
		}
		
		final Set<PREREQUISITE> subSet = sub instanceof Set<?> ? (Set<PREREQUISITE>)sub : new HashSet<>(sub);
		
		final Set<Prerequisite<?>> prerequisiteSet = new HashSet<>(subSet.size());

		if (sub.isEmpty()) {
			listener.accept(prerequisiteSet);
		}
		else {
		
			for (PREREQUISITE prerequisite : subSet) {
				
				if (prerequisiteSpec.getAction() != null) {
					
					final TargetSpec<CONTEXT, PREREQUISITE> subTargetSpec = prerequisiteSpec.getAction().getSubTarget();
					
					findTargets(
							fromPrerequisites,
							subTargetSpec,
							logContext,
							context,
							prerequisite,
							logger, indent + 1,
							subTarget -> {
	
						final Prerequisite<PREREQUISITE> subPrerequisite = new Prerequisite<>(logContext, prerequisite, subTarget);
						
						prerequisiteSet.add(subPrerequisite);
						
						if (prerequisiteSet.size() == subSet.size()) {
							listener.accept(prerequisiteSet);
						}
					});
				}
				else {
					
					final TargetReference<PREREQUISITE> targetReference = new TargetReference<PREREQUISITE>(
							logContext,
							null,
							prerequisite,
							null);
					
					final Prerequisite<PREREQUISITE> subPrerequisite = new Prerequisite<>(logContext, prerequisite, targetReference);
					
					prerequisiteSet.add(subPrerequisite);
	
					if (prerequisiteSet.size() == sub.size()) {
						listener.accept(prerequisiteSet);
					}
				}
			}
		}
	}
}
