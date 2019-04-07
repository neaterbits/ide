package com.neaterbits.ide.util.dependencyresolution.executor;

import java.util.function.Function;

import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

class PrerequisiteCompleteChecker {

	static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(ExecutorState<CONTEXT> targetState, Target<?> target) {
		
		return hasCompletedPrerequisites(targetState::getTargetCompletion, target);
	}
		
	static <CONTEXT extends TaskContext> PrerequisiteCompletion hasCompletedPrerequisites(Function<Target<?>, PrerequisiteCompletion> targetState, Target<?> target) {
		
		for (Prerequisites prerequisites : target.getPrerequisites()) {

			for (Prerequisite<?> prerequisite : prerequisites.getPrerequisites()) {
				
				if (prerequisite.getSubTarget() != null) {
					
					final PrerequisiteCompletion subStatus = hasCompletedPrerequisites(targetState, prerequisite.getSubTarget());
					
					if (subStatus.getStatus() != Status.SUCCESS) {

						return subStatus;
					}

					final PrerequisiteCompletion thisStatus = targetState.apply(prerequisite.getSubTarget());
					if (thisStatus.getStatus() != Status.SUCCESS) {
						return thisStatus;
					}
				}
			}
		}
		
		return new PrerequisiteCompletion(Status.SUCCESS);
	}
}
