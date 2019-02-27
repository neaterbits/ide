package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionFunction;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

public class TargetExecutor extends TargetAsyncExecutor {
	
	public <CONTEXT extends TaskContext, TARGET> void runTargets(CONTEXT context, Target<TARGET> rootTarget, TargetExecutorLogger logger) {
		
		final TargetState state = TargetState.createFromTargetTree(rootTarget);
		
		scheduleTargets(context, state, logger);
		
		runQueuedRunnables();
	}
	
	private <CONTEXT extends TaskContext> void scheduleTargets(CONTEXT context, TargetState state, TargetExecutorLogger logger) {
		
		if (!state.hasExecuteTargets()) {
			throw new IllegalStateException();
		}
		
		for (;;) {

			int targetsLeft = state.getNumExecuteTargets();
			
			final List<Target<?>> targets = new ArrayList<>(state.getExecuteTargets());
			
			for (Target<?> target : targets) {

				/*
				if (!state.toExecuteTargets.contains(target)) {
					// Removed asynchronously
					continue;
				}
				*/

				final Status status = hasCompletedPrerequisites(target, state.getCompletedTargets(), state.getFailedTargets());
				
				if (logger != null) {
					logger.onScheduleTarget(target, status);
				}
				
				if (status == Status.COMPLETE) {
					
					state.moveTargetFromToExecuteToScheduled(target);

					collectComputedTargets(target, state, logger);
					
					final Action<?> action = target.getAction();
					final ActionWithResult<?> actionWithResult = target.getActionWithResult();
					
					if (action != null) {
						runOrScheduleAction(action, context, target, state, logger);
					}
					else if (actionWithResult != null) {
						
						System.out.println("### actionWithResult");
						
						runOrScheduleActionWithResult(actionWithResult, context, target, state, logger);
					}
					else {
						onCompletedTarget(context, target, state, null, false, logger);
					}
				}
			}
			
			if (targetsLeft == state.getNumExecuteTargets()) {
				break;
			}
		}
	}

	private <CONTEXT extends TaskContext> void runOrScheduleAction(Action<?> action, CONTEXT context, Target<?> target, TargetState state, TargetExecutorLogger logger) {
		if (action.getConstraint() == null) {
			final Exception exception = performAction(action, context, target, state, logger);
			
			onCompletedTarget(context, target, state, exception, false, logger);
		}
		else {
			scheduler.schedule(
					action.getConstraint(),
					null,
					param -> {
						return performAction(action, context, target, state, logger);
					},
					(param, exception) -> {
						onCompletedTarget(context, target, state, exception, true, logger);
					} );
		}
	}
	
	private <CONTEXT extends TaskContext> void runOrScheduleActionWithResult(ActionWithResult<?> actionWithResult, CONTEXT context, Target<?> target, TargetState state, TargetExecutorLogger logger) {
		
		if (actionWithResult.getConstraint() == null) {
			
			final Result result = performAction(actionWithResult, context, target, state, logger);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final ProcessResult<CONTEXT, Object, Object> processResult = (ProcessResult)actionWithResult.getOnResult();
			
			processResult.process(context, target.getTargetObject(), result.result);
			
			onCompletedTarget(context, target, state, result.exception, false, logger);
		}
		else {
			scheduler.schedule(
					actionWithResult.getConstraint(),
					null,
					param -> {
						return performAction(actionWithResult, context, target, state, logger);
					},
					(param, result) -> {
						@SuppressWarnings({ "unchecked", "rawtypes" })
						final ProcessResult<CONTEXT, Object, Object> processResult = (ProcessResult)actionWithResult.getOnResult();
						
						processResult.process(context, target.getTargetObject(), result.result);
						
						onCompletedTarget(context, target, state, result.exception, true, logger);
					} );
		}
	}
	
	private <CONTEXT extends TaskContext> Exception performAction(Action<?> action, CONTEXT context, Target<?> target, TargetState state, TargetExecutorLogger logger) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final ActionFunction<CONTEXT, Object> actionFunction = (ActionFunction)action.getActionFunction();
		
		if (logger != null) {
			logger.onAction(target);
		}
		
		try {
			actionFunction.perform(context, target.getTargetObject(), state);
		} catch (Exception ex) {
			return ex;
		}
		
		return null;
	}
	private static class Result {
		private final Object result;
		private final Exception exception;

		public Result(Object result, Exception exception) {
			this.result = result;
			this.exception = exception;
		}
	}

	private <CONTEXT extends TaskContext> Result performAction(ActionWithResult<?> action, CONTEXT context, Target<?> target, TargetState state, TargetExecutorLogger logger) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final BiFunction<CONTEXT, Object, Object> actionFunction = (BiFunction)action.getActionWithResult();
		
		if (logger != null) {
			logger.onAction(target);
		}

		Exception exception = null;
		Object result = null;
		
		try {
			result = actionFunction.apply(context, target.getTargetObject());
		}
		catch (Exception ex) {
			exception = ex;
		}

		return new Result(result, exception);
	}

	
	private <CONTEXT extends TaskContext>
	void onCompletedTarget(CONTEXT context, Target<?> target, TargetState targetState, Exception exception, boolean async, TargetExecutorLogger logger) {

		
		if (logger != null) {
			logger.onComplete(target, exception);
		}
		
		targetState.onCompletedTarget(target, exception);
		
		if (async) {
			scheduleTargets(context, targetState, logger);
		}
	}

	private Status hasCompletedPrerequisites(Target<?> target, Set<Target<?>> completedTargets, Map<Target<?>, Exception> failedTargets) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				
				if (prerequisite.getSubTarget() != null) {
					
					final Status subStatus = hasCompletedPrerequisites(prerequisite.getSubTarget(), completedTargets, failedTargets);
					
					if (subStatus != Status.COMPLETE) {
						System.out.println("## missing substatus " + prerequisite.getSubTarget() + "/" + subStatus);

						return subStatus;
					}
					
					if (failedTargets.containsKey(prerequisite.getSubTarget())) {
						return Status.FAILED;
					}
					else if (!completedTargets.contains(prerequisite.getSubTarget())) {
						
						System.out.println("## missing subtarget " + prerequisite.getSubTarget());
						
						return Status.INCOMPLETE;
					}
					
				}

				if (prerequisites.isRecursiveBuild()) {
					System.out.println("################### recursive build for " + prerequisite + "/" + prerequisite.getSubTarget());
				}
			}
		}
		
		return Status.COMPLETE;
	}

	
	private void collectComputedTargets(Target<?> target, TargetState state, TargetExecutorLogger logger) {

		for (Prerequisites prerequisites : target.getPrerequisites()) {

			final List<Object> targetObjects = new ArrayList<>(prerequisites.getPrerequisites().size());
			
			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {

				final Target<?> subTarget = prerequisite.getSubTarget();

				if (subTarget != null) {
					
					final Object subCollected = state.getCollected(subTarget);
					
					if (subCollected != null) {
						targetObjects.add(subCollected);
					}
					else if (subTarget.getTargetObject() != null) {
						targetObjects.add(subTarget.getTargetObject());
					}
				}
				else {
					
					if (prerequisite.getItem() == null) {
						throw new IllegalStateException();
					}
					
					targetObjects.add(prerequisite.getItem());
				}
			}
			
			if (prerequisites.getCollect() != null) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final BiFunction<Object, List<Object>, Object> collect = (BiFunction)prerequisites.getCollect();
				
				final Object collected = collect.apply(target.getTargetObject(), targetObjects);
				
				if (logger != null) {
					logger.onCollect(target, targetObjects, collected);
				}
				
				if (collected != null) {
					state.addCollected(target, collected);
				}
			}
		}
	}
}

