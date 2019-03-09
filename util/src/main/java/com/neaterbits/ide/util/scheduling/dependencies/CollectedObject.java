package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;

public interface CollectedObject {

	String getName();
	
	List<String> getCollected();
	
}
