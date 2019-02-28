package com.neaterbits.ide.util.scheduling.dependencies;

import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilderImpl;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public abstract class TargetBuildSpec<CONTEXT extends TaskContext> {

	protected abstract void buildSpec(TargetBuilder<CONTEXT> targetBuilder);
	
	public final void execute(CONTEXT context) {
		
		final TargetBuilderImpl<CONTEXT> builderImpl = new TargetBuilderImpl<>();
		buildSpec(builderImpl);
		
		final TargetSpec<CONTEXT, ?, ?> targetSpec = builderImpl.build();
		
		final TargetFinder targetFinder = new TargetFinder();
		
		final TargetFinderLogger targetFinderLogger = null; // new PrintlnTargetFinderLogger();
		
		targetFinder.computeTargets(targetSpec, context, targetFinderLogger, target -> {
			
			target.printTargets();
			
			final TargetExecutor targetExecutor = new TargetExecutor();
			
			targetExecutor.runTargets(context, target, new PrintlnTargetExecutorLogger());
		});

	}
}
