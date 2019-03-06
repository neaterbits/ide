package com.neaterbits.ide.util.scheduling.dependencies.builder;


public interface PrerequisitesMap<TARGET> {

	<T> T getCollectedProduct(TARGET target, Class<T> type);
	
}
