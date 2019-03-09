package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

final class TargetExecutor {
	
	private static final Boolean DEBUG = false;
	
	private final AsyncExecutor asyncExecutor;
	
	TargetExecutor(AsyncExecutor asyncExecutor) {
		
		Objects.requireNonNull(asyncExecutor);
		
		this.asyncExecutor = asyncExecutor;
	}

	public <CONTEXT extends TaskContext, TARGET> void runTargets(
			CONTEXT context,
			Target<TARGET> rootTarget,
			TargetExecutorLogger logger,
			Consumer<TargetBuildResult> onResult) {
		
		final ExecutorState state = ExecutorState.createFromTargetTree(rootTarget);

		final TargetExecutionContext<CONTEXT> targetExecutionContext = new TargetExecutionContext<CONTEXT>(
				context,
				state,
				asyncExecutor,
				logger, 
				onResult);
		
		scheduleTargets(targetExecutionContext);
	}
	
	private <CONTEXT extends TaskContext> void scheduleTargets(TargetExecutionContext<CONTEXT> context) {
		
		if (!context.state.hasExecuteTargets()) {
			throw new IllegalStateException();
		}

		if (context.logger != null) {
			context.logger.onScheduleTargets(asyncExecutor.getNumScheduledJobs(), context.state);
		}
		
		asyncExecutor.runQueuedRunnables();

		final int priorToExecuteOrScheduled = context.state.getNumExecuteOrScheduledTargets();
		
		for (;;) {

			int targetsLeft = context.state.getNumExecuteTargets();
			
			final List<Target<?>> targets = new ArrayList<>(context.state.getToExecuteTargets());
			
			for (Target<?> target : targets) {

				final PrerequisiteCompletion status = hasCompletedPrerequisites(context.state, target);
				
				if (status.getStatus() == Status.SUCCESS) {
					
					if (context.logger != null) {
						context.logger.onScheduleTarget(target, status.getStatus(), context.state);
					}

					context.state.moveTargetFromToExecuteToScheduled(target);

					Collector.collectFromSubTargetsAndSubProducts(context, target);

					runAnyActionsAndCallOnCompleted(context, target);
				}
				else if (status.getStatus() == Status.FAILED) {
					context.state.moveTargetFromToExecuteToFailed(target);
					
					if (context.logger != null) {
						context.logger.onComplete(target, status.getException(), context.state);
					}
				}
			}
			
			if (targetsLeft == context.state.getNumExecuteTargets()) {
				break;
			}
		}
		
		if (
				context.onResult != null
			&&	context.state.getNumExecuteOrScheduledTargets() == 0
			&& priorToExecuteOrScheduled != 0
			&& asyncExecutor.getNumScheduledJobs() == 0) {

			context.onResult.accept(context.state);
		}
	}
	
	private <CONTEXT extends TaskContext> void runAnyActionsAndCallOnCompleted(TargetExecutionContext<CONTEXT> context, Target<?> target) {
		
		final Action<?> action = target.getAction();
		final ActionWithResult<?> actionWithResult = target.getActionWithResult();
		
		final BiConsumer<Exception, Boolean> onCompleted = (exception, async) -> {

			if (target.getFromPrerequisite() != null) {
				final Prerequisites fromPrerequisites = target.getFromPrerequisite().getFromPrerequisites();
	
				if (fromPrerequisites == null) {
					throw new IllegalStateException("## no prerequisites for target " + target.getTargetObject());
				}
				
				if (fromPrerequisites.isRecursiveBuild()) {
					addRecursiveBuildTargets(context.state, context.context, fromPrerequisites, target);
				}
			}
				
			onCompletedTarget(context, target, exception, async);
		};

		if (action != null) {
			Actions.runOrScheduleAction(context, action, target, onCompleted);
		}
		else if (actionWithResult != null) {
			Actions.runOrScheduleActionWithResult(context, actionWithResult, target, onCompleted);
		}
		else {
			onCompleted.accept(null, false);
		}
	}
	
	
	private static void addRecursiveBuildTargets(ExecutorState targetState, TaskContext context, Prerequisites fromPrerequisites, Target<?> target) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final BiFunction<Object, Object, Collection<Object>> getSubPrerequisites
			= (BiFunction)fromPrerequisites.getSubPrerequisitesFunction();

		@SuppressWarnings({ "unchecked", "rawtypes" })

		final Function<Object, Object> getTargetFromPrerequisite = (Function)fromPrerequisites.getTargetFromSubPrerequisite();
		
		final Object targetObject = getTargetFromPrerequisite.apply(target.getTargetObject());
		
		if (DEBUG) {
			System.out.println("## got target object " + targetObject + " from " + target.getTargetObject() + " of " + target.getTargetObject().getClass());
		}

		final Collection<Object> targetPrerequisites = getSubPrerequisites.apply(context, targetObject);

		/*
		if (targetPrerequisites.size() != target.getPrerequisites().size()) {
			throw new IllegalStateException("prerequisites mismatch for "  + target + " " + targetPrerequisites + "/" + target.getPrerequisites());
		}
		*/
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final TargetSpec<TaskContext, Object> targetSpec = (TargetSpec)target.getTargetSpec();

		final List<Prerequisite<?>> targetPrerequisitesList = new ArrayList<>(targetPrerequisites.size());
		
		for (Object subPrerequisiteObject : targetPrerequisites) {

			if (DEBUG) {
				System.out.println("## process sub prerequisite object " + subPrerequisiteObject);
			}
			
			final Collection<Object> subPrerequisitesList = getSubPrerequisites.apply(context, subPrerequisiteObject);

			final List<Prerequisite<?>> list = subPrerequisitesList.stream()
					.map(sp -> new Prerequisite<>(sp, null))
					.collect(Collectors.toList());
			
			final Prerequisites subPrerequisites = new Prerequisites(list, fromPrerequisites.getSpec());
			
			final Target<Object> subTarget =
					targetSpec.createTarget(
							context,
							subPrerequisiteObject,
							Arrays.asList(subPrerequisites));

			targetPrerequisitesList.add(new Prerequisite<>(subPrerequisiteObject, subTarget));

			if (DEBUG) {
				System.out.println("## added subtarget " + subTarget + " from prerequisites " + targetPrerequisites + " from " + target.getTargetObject());
			}
			
			if (!targetState.hasTarget(subTarget)) {
				targetState.addTargetToExecute(subTarget);
			}
			
			if (DEBUG) {
				System.out.println("## added subtarget done");
			}
		}
		
		// Trigger fromPrerequisite to be set in sub targets
		final Prerequisites updatedPrerequisites = new Prerequisites(targetPrerequisitesList, fromPrerequisites.getSpec());
		
		target.setPrerequisites(Arrays.asList(updatedPrerequisites));
		
		/*
		final Target<?> replaceTarget = targetSpec.createTarget(
				context,
				target.getTargetObject(),
				Arrays.asList(new Prerequisites(targetPrerequisitesList, prerequisites.getSpec())));
		
		targetState.replaceTarget(target, replaceTarget);
		*/
		
	}
	
	private <CONTEXT extends TaskContext>
	void onCompletedTarget(
			TargetExecutionContext<CONTEXT> context,
			Target<?> target,
			Exception exception,
			boolean async) {

		
		if (context.logger != null) {
			context.logger.onComplete(target, exception, context.state);
		}
		
		context.state.onCompletedTarget(target, exception);
		
		if (async) {
			scheduleTargets(context);
		}
	}

	private PrerequisiteCompletion hasCompletedPrerequisites(ExecutorState targetState, Target<?> target) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				
				if (prerequisite.getSubTarget() != null) {
					
					final PrerequisiteCompletion subStatus = hasCompletedPrerequisites(targetState, prerequisite.getSubTarget());
					
					if (subStatus.getStatus() != Status.SUCCESS) {
						// System.out.println("## missing substatus " + prerequisite.getSubTarget() + "/" + subStatus);

						return subStatus;
					}

					return targetState.getTargetCompletion(prerequisite.getSubTarget());
				}
			}
		}
		
		return new PrerequisiteCompletion(Status.SUCCESS);
	}
}

