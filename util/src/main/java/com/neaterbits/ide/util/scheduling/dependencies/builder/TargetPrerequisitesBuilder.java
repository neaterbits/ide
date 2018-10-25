package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface TargetPrerequisitesBuilder<CONTEXT extends TaskContext, TARGET>
	extends TargetIteratingBuilder<CONTEXT, TARGET> {

	<PRODUCT>
	PrerequisitesProductBuilder<CONTEXT, TARGET, PRODUCT> product(Class<PRODUCT> productType);
	
}
