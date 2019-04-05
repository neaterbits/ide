package com.neaterbits.ide.util.scheduling.dependencies;


import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;
import com.neaterbits.ide.util.statemachine.StateMachine;

public class TargetStateMachine<CONTEXT extends TaskContext>
	extends StateMachine<BaseTargetState<CONTEXT>> {

	private final Target<?> target;
	
	TargetStateMachine(Target<?> target) {
		super(new TargetStateToExecute<>(target));
	
		this.target = target;
	}

	@Override
	protected String getObjectDebugString() {
		return target.getDebugString();
	}
}
