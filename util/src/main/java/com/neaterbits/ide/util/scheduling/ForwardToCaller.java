package com.neaterbits.ide.util.scheduling;

public interface ForwardToCaller {

	void forward(Runnable runnable);
	
}
