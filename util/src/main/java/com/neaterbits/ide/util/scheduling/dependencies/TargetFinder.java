package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

final class TargetFinder extends PrerequisitesFinder {

	TargetFinder(AsyncExecutor asyncExecutor) {

		super(asyncExecutor);
	}

	<CONTEXT extends TaskContext, TARGET> void computeTargets(
			List<TargetSpec<CONTEXT, TARGET>> targetSpecs,
			CONTEXT context,
			TargetFinderLogger logger,
			Consumer<Target<TARGET>> rootTarget) {

		for (TargetSpec<CONTEXT, TARGET> targetSpec : targetSpecs) {
			findTargets(null, targetSpec, context, null, logger, 0, rootTarget);
		}

		asyncExecutor.runQueuedResultRunnables();
	}

	@Override
	<CONTEXT extends TaskContext, TARGET>
		void findTargets(
				Prerequisites fromPrerequisites,
				TargetSpec<CONTEXT, TARGET> targetSpec,
				CONTEXT context,
				TARGET target,
				TargetFinderLogger logger,
				int indent,
				Consumer<Target<TARGET>> targetCreated) {
		
		if (logger != null) {
			logger.onFindTarget(indent, context, targetSpec, target);
		}

		final Consumer<List<Prerequisites>> onFoundPrerequisites = (List<Prerequisites> prerequisites) -> {
			
			final Target<TARGET> createdTarget = targetSpec.createTarget(context, target, prerequisites);
			
			if (logger != null) {
				logger.onFoundPrerequisites(indent, createdTarget, prerequisites);
			}
			
			targetCreated.accept(createdTarget);
		};
		
		findPrerequisites(
				context,
				targetSpec,
				target,
				targetSpec.getPrerequisiteSpecs(),
				logger, indent + 1,
				onFoundPrerequisites);
	}

	private <CONTEXT extends TaskContext, TARGET, FILE_TARGET, PREREQUISITE> void findPrerequisites(
			CONTEXT context,
			TargetSpec<CONTEXT, TARGET> targetSpec,
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
	
				getPrerequisites(context, null, targetSpec, target, prerequisiteSpec, logger, indent, prerequisitesList -> {
					
					final Prerequisites prerequisites = new Prerequisites(prerequisitesList, prerequisiteSpec);
					
					list.add(prerequisites);
	
					if (list.size() == prerequisiteSpecs.size()) {
						onResult.accept(list);
					}
				});
			}
		}
	}
}
