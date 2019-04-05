package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisitesProductBuilder<CONTEXT extends TaskContext, TARGET, PRODUCT> {
	
	<ITEM>
	PrerequisitesItemBuilder<CONTEXT, TARGET, PRODUCT, ITEM> fromItemType(Class<ITEM> itemType);
	
}
