package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisiteCollectActionBuilder<CONTEXT extends TaskContext, TARGET, PREREQUISITE, PRODUCT, ITEM> {

	PrerequisitesOrCollectBuilder<CONTEXT, TARGET, PRODUCT, ITEM>
		buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets);

}
