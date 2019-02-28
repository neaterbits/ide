package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

final class TargetFinder {

	private final AsyncExecutor asyncExecutor;

	TargetFinder(AsyncExecutor asyncExecutor) {

		Objects.requireNonNull(asyncExecutor);
		
		this.asyncExecutor = asyncExecutor;
	}

	<CONTEXT extends TaskContext, TARGET, FILE_TARGET> void computeTargets(
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			CONTEXT context,
			TargetFinderLogger logger,
			Consumer<Target<TARGET>> rootTarget) {

		findTargets(targetSpec, context, null, logger, 0, rootTarget);

		asyncExecutor.runQueuedRunnables();
	}

	private <CONTEXT extends TaskContext, TARGET, FILE_TARGET>
		void findTargets(
				TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
				CONTEXT context,
				TARGET target,
				TargetFinderLogger logger,
				int indent,
				Consumer<Target<TARGET>> targetCreated) {
		
		if (logger != null) {
			logger.onFindTarget(indent, context, targetSpec, target);
		}

		findPrerequisites(context, targetSpec, target, targetSpec.getPrerequisites(), logger, indent + 1, result -> {

			final Target<TARGET> createdTarget;
			
			final String description = targetSpec.getDescription(target);
			
			if (targetSpec.getName() != null) {
				createdTarget = new NamedTarget<>(
						targetSpec.getType(),
						targetSpec.getName(),
						description,
						target,
						result,
						targetSpec.makeAction(),
						targetSpec.makeActionWithResult());
			}
			else if (targetSpec.getFile() != null) {
				
				final FILE_TARGET fileTarget = targetSpec.getFileTarget().apply(context, target);
				
				createdTarget = new FileTarget<>(
						targetSpec.getType(),
						targetSpec.getFile().apply(fileTarget),
						description,
						target,
						result,
						targetSpec.makeAction(),
						targetSpec.makeActionWithResult());
			}
			else {
				throw new UnsupportedOperationException();
			}
			
			if (logger != null) {
				logger.onFoundPrerequisites(indent, createdTarget, result);
			}
			
			targetCreated.accept(createdTarget);
		});
	}

	private <CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE> void findPrerequisites(
			CONTEXT context,
			TargetSpec<CONTEXT, TARGET, FILE_TARGET> targetSpec,
			TARGET target,
			List<PrerequisiteSpec<CONTEXT, TARGET, ?>> prerequisiteSpecs,
			TargetFinderLogger logger,
			int indent,
			Consumer<List<Prerequisites>> onResult) {

		final List<Prerequisites> list = new ArrayList<>(prerequisiteSpecs.size());

		if (prerequisiteSpecs.isEmpty()) {
			onResult.accept(list);
		}
		else {
			for (PrerequisiteSpec<CONTEXT, TARGET, ?> prerequisiteSpec : prerequisiteSpecs) {
	
				getPrerequisites(context, targetSpec, target, prerequisiteSpec, logger, indent, prerequisitesList -> {
					
					final Prerequisites prerequisites = new Prerequisites(
							prerequisitesList,
							prerequisiteSpec.getTargetFromPrerequisite(),
							prerequisiteSpec.isRecursiveBuild(),
							prerequisiteSpec.getCollect());
					
					list.add(prerequisites);
	
					if (list.size() == prerequisiteSpecs.size()) {
						onResult.accept(list);
					}
				});
			}
		}
	}

	private <CONTEXT extends TaskContext, TARGET, PREREQUISITE>
	void getPrerequisites(
			CONTEXT context,
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

						getPrerequisites(context, targetSpec, target, prerequisiteSpec, result, logger, indent, prerequisites -> {
						
							if (logger != null) {
								logger.onPrerequisites(indent, targetSpec, target, prerequisiteSpec, prerequisites);
							}
														
							listener.accept(prerequisites);
						});
					});
		} else {
			final Collection<PREREQUISITE> sub = prerequisiteSpec.getPrerequisites(context, target);

			getPrerequisites(context, targetSpec, target, prerequisiteSpec, sub, logger, indent, prerequisites -> {
				
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
					
					findTargets(subTargetSpec, context, prerequisite, logger, indent + 1, subTarget -> {
	
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
