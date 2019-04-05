package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.statemachine.StateMachine;

public class TargetExecutorStateMachine extends StateMachine<BaseTargetExecutorState> {

	public TargetExecutorStateMachine(BaseTargetExecutorState initialState) {
		super(initialState);
	}

	@Override
	protected String getObjectDebugString() {
		return null;
	}

}
