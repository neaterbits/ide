package com.neaterbits.ide.util.scheduling;

public interface ForwardResultToCaller {

	void forward(Runnable runnable);
	
}
