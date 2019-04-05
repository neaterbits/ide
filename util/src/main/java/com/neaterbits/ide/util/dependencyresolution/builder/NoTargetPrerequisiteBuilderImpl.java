package com.neaterbits.ide.util.dependencyresolution.builder;

import java.util.function.Consumer;

import com.neaterbits.ide.util.dependencyresolution.spec.BuildSpec;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public class NoTargetPrerequisiteBuilderImpl<CONTEXT extends TaskContext, PREREQUISITE>
		implements NoTargetPrerequisiteBuilder<CONTEXT, PREREQUISITE> {

	private final PrerequisiteBuilderState<CONTEXT, Object, Void, Void> prerequisiteBuilderState;
	
	public NoTargetPrerequisiteBuilderImpl(
			PrerequisiteBuilderState<CONTEXT, Object, Void, Void> prerequisiteBuilderState) {
		this.prerequisiteBuilderState = prerequisiteBuilderState;
	}

	@Override
	public void buildBy(Consumer<TypedSubTargetBuilder<CONTEXT, PREREQUISITE>> prerequisiteTargets) {

		final TypedSubTargetBuilderImpl<CONTEXT, PREREQUISITE> sub = new TypedSubTargetBuilderImpl<>(); 
		
		prerequisiteTargets.accept(sub);
		
		final TargetBuilderState<CONTEXT, PREREQUISITE, ?> subTargetBuilderState = sub.build();
		
		prerequisiteBuilderState.setBuild(new BuildSpec<>(subTargetBuilderState.build()));
	}
}
