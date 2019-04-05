package com.neaterbits.ide.util.dependencyresolution.builder;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface TargetPrerequisitesBuilder<CONTEXT extends TaskContext, TARGET>
	extends TargetIteratingBuilder<CONTEXT, TARGET> {

	<PRODUCT>
	PrerequisitesProductBuilder<CONTEXT, TARGET, PRODUCT> makingProduct(Class<PRODUCT> productType);
	
}
