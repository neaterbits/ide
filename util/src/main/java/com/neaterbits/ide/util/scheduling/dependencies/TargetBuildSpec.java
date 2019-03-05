package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilder;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TargetBuilderImpl;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public abstract class TargetBuildSpec<CONTEXT extends TaskContext> {

	protected abstract void buildSpec(TargetBuilder<CONTEXT> targetBuilder);
	
	public List<TargetSpec<CONTEXT, ?, ?>> buildTargetSpecs() {

		final TargetBuilderImpl<CONTEXT> builderImpl = new TargetBuilderImpl<>();
		
		buildSpec(builderImpl);

		return builderImpl.build();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void execute(CONTEXT context, TargetExecutorLogger logger, Consumer<TargetBuildResult> onResult) {
		
		try {
			final List<TargetSpec<CONTEXT, ?, ?>> targetSpecs = buildTargetSpecs();
			
			final AsyncExecutor asyncExecutor = new AsyncExecutor();
			
			final TargetFinder targetFinder = new TargetFinder(asyncExecutor);
			
			final TargetFinderLogger targetFinderLogger = null; // new PrintlnTargetFinderLogger();
			
			targetFinder.computeTargets((List)targetSpecs, context, targetFinderLogger, target -> {
				
				// target.printTargets();
				
				final TargetExecutor targetExecutor = new TargetExecutor(asyncExecutor);
				
				targetExecutor.runTargets(context, target, logger, onResult);
			});
		}
		catch (Throwable ex) {
			ex.printStackTrace();
			
			throw ex;
		}
	}
}
