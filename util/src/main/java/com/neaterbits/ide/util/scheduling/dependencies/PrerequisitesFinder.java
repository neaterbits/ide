package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

abstract class PrerequisitesFinder {

	final AsyncExecutor asyncExecutor;
	
	PrerequisitesFinder(AsyncExecutor asyncExecutor) {
		
		Objects.requireNonNull(asyncExecutor);

		this.asyncExecutor = asyncExecutor;
	}

	abstract <CONTEXT extends TaskContext, TARGET, FILE_TARGET>
	void findTargets(
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			CONTEXT context,
			TARGET target,
			TargetFinderLogger logger,
			int indent,
			Consumer<Target<TARGET>> targetCreated);
	
	final <CONTEXT extends TaskContext, TARGET, PREREQUISITE>
	void getPrerequisites(
			CONTEXT context,
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET, ?> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			TargetFinderLogger logger,
			int indent,
			Consumer<List<Prerequisite<?>>> listener) {
		
		if (prerequisiteSpec.getConstraint() != null) {

			asyncExecutor.schedule(
					prerequisiteSpec.getConstraint(),
					null,
					param -> {
						return prerequisiteSpec.getPrerequisites(context, target);
					},
					(param, result) -> {

						getPrerequisites(context, fromPrerequisites, targetSpec, target, prerequisiteSpec, result, logger, indent, prerequisites -> {
						
							if (logger != null) {
								logger.onPrerequisites(indent, targetSpec, target, prerequisiteSpec, prerequisites);
							}
														
							listener.accept(prerequisites);
						});
					});
		} else {
			final Collection<PREREQUISITE> sub = prerequisiteSpec.getPrerequisites(context, target);

			getPrerequisites(context, fromPrerequisites, targetSpec, target, prerequisiteSpec, sub, logger, indent, prerequisites -> {
				
				if (logger != null) {
					logger.onPrerequisites(indent, targetSpec, target, prerequisiteSpec, prerequisites);
				}
				
				listener.accept(prerequisites);
			});
		}
	}

	private <CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE>
	void getPrerequisites(
			CONTEXT context,
			Prerequisites fromPrerequisites,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			TARGET target,
			PrerequisiteSpec<CONTEXT, TARGET, PREREQUISITE> prerequisiteSpec,
			Collection<PREREQUISITE> sub,
			TargetFinderLogger logger,
			int indent,
			Consumer<List<Prerequisite<?>>> listener) {

		if (sub == null) {
			throw new IllegalStateException("No prerequisites for " + targetSpec.getType().getSimpleName() + "/" + target + "/" + prerequisiteSpec.getDescription());
		}
		
		final List<Prerequisite<?>> list = new ArrayList<>(sub.size());

		if (sub.isEmpty()) {
			listener.accept(list);
		}
		else {
		
			for (PREREQUISITE prerequisite : sub) {
				
				if (prerequisiteSpec.getAction() != null) {
					
					final TargetSpec<CONTEXT, PREREQUISITE, ?> subTargetSpec = prerequisiteSpec.getAction().getSubTarget();
					
					findTargets(
							fromPrerequisites,
							subTargetSpec,
							context,
							prerequisite,
							logger, indent + 1,
							subTarget -> {
	
						final Prerequisite<PREREQUISITE> subPrerequisite = new Prerequisite<>(prerequisite, subTarget);
						
						list.add(subPrerequisite);
						
						if (list.size() == sub.size()) {
							listener.accept(list);
						}
					});
				}
				else {
					final Prerequisite<PREREQUISITE> subPrerequisite = new Prerequisite<>(prerequisite, null);
					
					list.add(subPrerequisite);
	
					if (list.size() == sub.size()) {
						listener.accept(list);
					}
				}
			}
		}
	}
}