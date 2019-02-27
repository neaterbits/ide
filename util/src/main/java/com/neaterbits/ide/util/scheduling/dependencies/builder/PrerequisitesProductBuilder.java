package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface PrerequisitesProductBuilder<CONTEXT extends TaskContext, TARGET, PRODUCT> {
	
	<ITEM>
	PrerequisitesItemBuilder<CONTEXT, TARGET, PRODUCT, ITEM> fromItemType(Class<ITEM> itemType);
	
}
