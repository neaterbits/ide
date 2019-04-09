package com.neaterbits.ide.util.dependencyresolution.executor;


import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.TargetDefinition;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.ide.util.statemachine.StateMachine;

public class TargetStateMachineBase<CONTEXT extends TaskContext>
	extends StateMachine<BaseTargetState<CONTEXT>> {

	private final TargetDefinition<?> target;
	
	TargetStateMachineBase(TargetDefinition<?> target, TargetExecutorLogger logger) {
		super(new TargetStateToExecute<>(target, logger));
	
		this.target = target;
	}

	@Override
	protected String getObjectDebugString() {
		return target.getDebugString();
	}
}
