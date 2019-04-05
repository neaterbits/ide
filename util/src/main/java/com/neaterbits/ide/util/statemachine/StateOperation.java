package com.neaterbits.ide.util.statemachine;

public interface StateOperation<STATE> {

	STATE execute(STATE curState);
	
}
