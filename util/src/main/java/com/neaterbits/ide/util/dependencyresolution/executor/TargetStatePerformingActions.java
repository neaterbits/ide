package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.TargetSpec;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.structuredlog.binary.logging.LogContext;

final class TargetStatePerformingActions<CONTEXT extends TaskContext> extends BaseTargetState<CONTEXT> {

	private static final Boolean DEBUG = false;
	
	TargetStatePerformingActions(Target<?> target) {
		super(target);
	}

	@Override
	public BaseTargetState<CONTEXT> onActionPerformed(TargetExecutionContext<CONTEXT> context) {
	
		final boolean targetsAdded = addRecursiveBuildTargetsIfAny(context.state, context.context, target);
		
		final BaseTargetState<CONTEXT> nextState;
		
		if (targetsAdded) {
			//context.state.moveTargetFromToScheduledToActionPerformedCollect(target);
			
			nextState = new TargetStateRecursiveTargets<>(target);
		}
		else {
			onCompletedTarget(context, target, null, false);
			
			nextState = new TargetStateDone<>(target);
		}
		
		return nextState;
	}
	
	@Override
	public BaseTargetState<CONTEXT> onActionError(TargetExecutionContext<CONTEXT> context, Exception ex) {

		onCompletedTarget(context, target, ex, false);
		
		return new TargetStateFailed<>(target, ex);
	}

	@Override
	Status getStatus() {
		return Status.SCHEDULED;
	}


	private 
	boolean addRecursiveBuildTargetsIfAny(ExecutorState<CONTEXT> targetState, TaskContext context, Target<?> target) {

		final boolean added;
		
		if (target.getFromPrerequisite() != null) {
			final Prerequisites fromPrerequisites = target.getFromPrerequisite().getFromPrerequisites();

			if (fromPrerequisites == null) {
				throw new IllegalStateException("## no prerequisites for target " + target.getTargetObject());
			}
			
			if (fromPrerequisites.isRecursiveBuild()) {
				addRecursiveBuildTargets(targetState, context, fromPrerequisites, target);

				added = true;
			}
			else {
				added = false;
			}
		}
		else {
			added = false;
		}
		
		return added;
	}
	
	private
	void addRecursiveBuildTargets(ExecutorState<CONTEXT> targetState, TaskContext context, Prerequisites fromPrerequisites, Target<?> target) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final BiFunction<Object, Object, Collection<Object>> getSubPrerequisites
			= (BiFunction)fromPrerequisites.getRecursiveBuildInfo().getSubPrerequisitesFunction();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Function<Object, Object> getTargetFromPrerequisite = (Function)fromPrerequisites.getRecursiveBuildInfo().getTargetFromPrerequisiteFunction();
		
		final Object targetObject = getTargetFromPrerequisite.apply(target.getTargetObject());
		
		final LogContext logContext = target.getLogContext();
		
		if (DEBUG) {
			System.out.println("## got target object " + targetObject + " from " + target.getTargetObject() + " of " + target.getTargetObject().getClass());
		}

		final Collection<Object> targetPrerequisites = getSubPrerequisites.apply(context, targetObject);

		/*
		if (targetPrerequisites.size() != target.getPrerequisites().size()) {
			throw new IllegalStateException("prerequisites mismatch for "  + target + " " + targetPrerequisites + "/" + target.getPrerequisites());
		}
		*/
		
		final List<Prerequisite<?>> targetPrerequisitesList = new ArrayList<>(targetPrerequisites.size());
		
		for (Object subPrerequisiteObject : targetPrerequisites) {

			if (DEBUG) {
				System.out.println("## process sub prerequisite object " + subPrerequisiteObject);
			}
			
			final Collection<Object> subPrerequisitesList = getSubPrerequisites.apply(context, subPrerequisiteObject);

			final List<Prerequisite<?>> list = subPrerequisitesList.stream()
					.map(sp -> new Prerequisite<>(logContext, sp, null))
					.collect(Collectors.toList());
			
			final Prerequisites subPrerequisites = new Prerequisites(
					logContext,
					list,
					fromPrerequisites.getDescription(),
					fromPrerequisites.getRecursiveBuildInfo(),
					fromPrerequisites.getCollectors());
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Target<Object> subTarget =
					((Target)target).createTarget(
							logContext,
							context,
							subPrerequisiteObject,
							Arrays.asList(subPrerequisites));

			targetPrerequisitesList.add(new Prerequisite<>(logContext, subPrerequisiteObject, subTarget));

			if (DEBUG) {
				System.out.println("## added subtarget " + subTarget + " from prerequisites " + targetPrerequisites + " from " + target.getTargetObject());
			}
			
			if (!targetState.hasTarget(subTarget)) {
				if (DEBUG) {
					System.out.println("## added target to execute " + subTarget);
				}

				targetState.addTargetToExecute(subTarget);
			}
			
			if (DEBUG) {
				System.out.println("## added subtarget done");
			}
		}
		
		// Trigger fromPrerequisite to be set in sub targets
		final Prerequisites updatedPrerequisites = new Prerequisites(
				logContext,
				targetPrerequisitesList,
				fromPrerequisites.getDescription(),
				fromPrerequisites.getRecursiveBuildInfo(),
				fromPrerequisites.getCollectors());
		
		target.updatePrerequisites(Arrays.asList(updatedPrerequisites));
		
		/*
		final Target<?> replaceTarget = targetSpec.createTarget(
				context,
				target.getTargetObject(),
				Arrays.asList(new Prerequisites(targetPrerequisitesList, prerequisites.getSpec())));
		
		targetState.replaceTarget(target, replaceTarget);
		*/
		
	}


	@Override
	public BaseTargetState<CONTEXT> onCheckPrerequisitesComplete(TargetExecutionContext<CONTEXT> context) {

		// not applicable to this state
		
		return this;
	}

}
