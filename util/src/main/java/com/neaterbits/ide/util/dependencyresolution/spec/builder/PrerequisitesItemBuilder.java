package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisitesItemBuilder<CONTEXT extends TaskContext, TARGET, PRODUCT, ITEM> 
	extends TargetCollectIteratingBuilder<CONTEXT, TARGET, PRODUCT, ITEM> {

}
