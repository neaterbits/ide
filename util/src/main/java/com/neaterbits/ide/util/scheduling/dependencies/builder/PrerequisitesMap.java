package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface PrerequisitesMap {

	<T> T getCollected(Class<T> type);
	
}
