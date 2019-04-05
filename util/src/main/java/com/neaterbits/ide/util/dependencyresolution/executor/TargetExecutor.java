package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public final class TargetExecutor {
	
	private final AsyncExecutor asyncExecutor;
	
	public TargetExecutor(AsyncExecutor asyncExecutor) {
		
		Objects.requireNonNull(asyncExecutor);
		
		this.asyncExecutor = asyncExecutor;
	}

	public <CONTEXT extends TaskContext, TARGET> void runTargets(
			CONTEXT context,
			Target<TARGET> rootTarget,
			TargetExecutorLogger logger,
			Consumer<TargetBuildResult> onResult) {
		
		final ExecutorState<CONTEXT> state = ExecutorState.createFromTargetTree(rootTarget, this);

		final TargetExecutionContext<CONTEXT> targetExecutionContext = new TargetExecutionContext<CONTEXT>(
				context,
				state,
				asyncExecutor,
				logger, 
				onResult);
		
		scheduleTargets(targetExecutionContext);
	}
	
	<CONTEXT extends TaskContext> void scheduleTargets(TargetExecutionContext<CONTEXT> context) {

		if (context.logger != null) {
			context.logger.onScheduleTargets(asyncExecutor.getNumScheduledJobs(), context.state);
		}
		
		while (!context.state.getNonCompletedTargets().isEmpty() || asyncExecutor.getNumScheduledJobs() != 0) {

			asyncExecutor.runQueuedResultRunnables();
		
			final List<TargetState<CONTEXT>> targets = new ArrayList<TargetState<CONTEXT>>(context.state.getNonCompletedTargets());

			final int targetsLeft = targets.size();
			final int priorNumScheduled = asyncExecutor.getNumScheduledJobs();
			
			for (TargetState<CONTEXT> target : targets) {
				
				// Try to check if state can be completed
				target.schedule(state -> state.onCheckPrerequisitesComplete(context));
			}

			if (   targetsLeft == context.state.getNonCompletedTargets().size()
				&& priorNumScheduled == asyncExecutor.getNumScheduledJobs()) {

				// No target scheduled
				System.err.println("Not able to trigger more target builds: " + context.state.getNonCompletedTargets().size());
				
				for (TargetState<CONTEXT> targetState : context.state.getNonCompletedTargets()) {
					
					final List<String> prerequisitesList = targetState.getTarget().getPrerequisites().stream()
							.flatMap(prerequisites -> prerequisites.getPrerequisites().stream())
							.map(prerequisite -> prerequisite.getSubTarget().getDebugString())
							.collect(Collectors.toList());
					
					System.err.println("Target " + targetState.getObjectDebugString()
						+ " in state " + targetState.getCurStateName()
						+ " with prerequisites " + prerequisitesList);
				}
				
				break;
			}
		}

		if (context.onResult != null) {
			context.onResult.accept(context.state);
		}
	}
}

