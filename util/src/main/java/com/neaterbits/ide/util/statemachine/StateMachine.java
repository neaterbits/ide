package com.neaterbits.ide.util.statemachine;

import java.util.LinkedList;
import java.util.Objects;

public abstract class StateMachine<STATE extends BaseState<STATE>> {

	private final LinkedList<StateOperation<STATE>> queue;
	private STATE curState;

	private boolean withinSchedule;
	
	protected abstract String getObjectDebugString();
	
	public StateMachine(STATE initialState) {
		
		Objects.requireNonNull(initialState);
		
		this.queue = new LinkedList<>();
		
		initialState.init(this);
		
		this.curState = initialState;
	}
	
	public final STATE getCurState() {
		return curState;
	}

	public final String getCurStateName() {
		return curState.getClass().getSimpleName();
	}

	public boolean schedule(StateOperation<STATE> runnable) {

		Objects.requireNonNull(runnable);
		
		boolean stateChangeOccured = false;
		
		queue.addLast(runnable);
		
		if (withinSchedule) {
			// schedule from within existing scheduling so run queued from within code below when exits
		}
		else {
		
			this.withinSchedule = true;
			
			try {

				while (!queue.isEmpty()) {
					
					final StateOperation<STATE> toExecute = queue.removeFirst();
					
					final STATE nextState = toExecute.execute(curState);
					
					if (nextState == null) {
						throw new IllegalStateException();
					}
		
					if (nextState != curState) {
						nextState.init(this);

						/*
						System.out.println("## setState from " + curState.getClass().getSimpleName() + " to "
								+ nextState.getClass().getSimpleName()
								+ " for event " + Thread.currentThread().getStackTrace()[2].getMethodName()
								+ " object " + getObjectDebugString());
						*/

						this.curState = nextState;
						
						stateChangeOccured = true;
					}
				}
			}
			finally {
				this.withinSchedule = false;
			}
		}
		
		return stateChangeOccured;
	}
}
