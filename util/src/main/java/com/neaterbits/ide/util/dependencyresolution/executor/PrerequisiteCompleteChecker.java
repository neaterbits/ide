package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.function.Function;

import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;
import com.neaterbits.ide.util.dependencyresolution.model.TargetReference;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

class PrerequisiteCompleteChecker {

	static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(
			ExecutorState<CONTEXT> targetState,
			TargetDefinition<?> target) {
		
		return hasCompletedPrerequisites(targetState::getTargetCompletion, targetState::getTargetDefinition, targetState::printTargetKeys, target);
	}

	static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(
			Function<TargetDefinition<?>, PrerequisiteCompletion> targetState,
			Function<TargetReference<?>, TargetDefinition<?>> getTargetDefiniton,
			TargetDefinition<?> target) {
		
		return hasCompletedPrerequisites(targetState, getTargetDefiniton, null, target);
	}
		

	private static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(
			Function<TargetDefinition<?>, PrerequisiteCompletion> targetState,
			Function<TargetReference<?>, TargetDefinition<?>> getTargetDefiniton,
			Runnable printTargetKeys,
			TargetDefinition<?> target) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				
				if (prerequisite.getSubTarget() != null) {
					
					final TargetDefinition<?> targetDefinition = getTargetDefiniton.apply(prerequisite.getSubTarget());
					
					if (targetDefinition == null) {
						
						if (printTargetKeys != null) {
							printTargetKeys.run();
						}

						throw new IllegalStateException("No target definition found for target reference " + prerequisite.getSubTarget()
									+ " of type " + prerequisite.getSubTarget().getTargetObject().getClass().getName());
					}
					
					final PrerequisiteCompletion subStatus = hasCompletedPrerequisites(
							targetState,
							getTargetDefiniton,
							printTargetKeys,
							targetDefinition);
					
					if (subStatus.getStatus() != Status.SUCCESS) {

						return subStatus;
					}

					final PrerequisiteCompletion thisStatus = targetState.apply(targetDefinition);
					if (thisStatus.getStatus() != Status.SUCCESS) {
						return thisStatus;
					}
				}
			}
		}
		
		return new PrerequisiteCompletion(Status.SUCCESS);
	}
}
