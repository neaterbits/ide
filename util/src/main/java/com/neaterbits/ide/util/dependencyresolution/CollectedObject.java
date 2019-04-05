package com.neaterbits.ide.util.dependencyresolution;

import java.util.List;

public interface CollectedObject {

	String getName();
	
	List<String> getCollected();
	
}
