package com.neaterbits.ide.util.dependencyresolution.builder;


public interface PrerequisitesMap<TARGET> {

	<T> T getCollectedProduct(TARGET target, Class<T> type);
	
}
