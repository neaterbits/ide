package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface PrerequisitesItemBuilder<CONTEXT extends TaskContext, TARGET, PRODUCT, ITEM> 
	extends TargetCollectIteratingBuilder<CONTEXT, TARGET, PRODUCT, ITEM> {

}
