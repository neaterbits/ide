package com.neaterbits.ide.util.dependencyresolution;

import java.util.ArrayList;
import java.util.List;

public abstract class BuildEntity {

	abstract BuildEntity getFromEntity();
	
	abstract String getDebugString();
	
	final List<String> getPath() {
		
		final List<String> list = new ArrayList<>();

		getPath(list);
		
		return list;
	}
	
	
	final void getPath(List<String> list) {
		
		final BuildEntity fromEntity = getFromEntity();
		
		
		if (fromEntity != null) {
			fromEntity.getPath(list);
		}

		final String debugString = getDebugString();
		// System.out.println("## add " + debugString);
		
		if (debugString != null) {
			list.add(debugString);
		}
	}
}
