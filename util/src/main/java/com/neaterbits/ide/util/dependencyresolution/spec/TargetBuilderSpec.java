package com.neaterbits.ide.util.dependencyresolution.spec;

import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.ide.util.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.ide.util.dependencyresolution.executor.TargetExecutor;
import com.neaterbits.ide.util.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.TargetBuilderImpl;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.structuredlog.binary.logging.LogContext;

public abstract class TargetBuilderSpec<CONTEXT extends TaskContext> {

	protected abstract void buildSpec(TargetBuilder<CONTEXT> targetBuilder);
	
	public List<TargetSpec<CONTEXT, ?>> buildTargetSpecs() {

		final TargetBuilderImpl<CONTEXT> builderImpl = new TargetBuilderImpl<>();
		
		buildSpec(builderImpl);

		return builderImpl.build();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void execute(LogContext logContext, CONTEXT context, TargetExecutorLogger logger, AsyncExecutor executor, Consumer<TargetBuildResult> onResult) {
		
		try {
			final List<TargetSpec<CONTEXT, ?>> targetSpecs = buildTargetSpecs();
			
			final TargetFinder targetFinder = new TargetFinder(executor);
			
			final TargetFinderLogger targetFinderLogger = null; // new PrintlnTargetFinderLogger();
			
			targetFinder.computeTargets((List)targetSpecs, logContext, context, targetFinderLogger, targetReference -> {
				
				targetReference.logRootObject(logContext);
				
				// target.printTargets();
				
				final TargetExecutor targetExecutor = new TargetExecutor(executor);
				
				targetExecutor.runTargets(context, targetReference.getTargetDefinitionIfAny(), logger, onResult);
			});
		}
		catch (Throwable ex) {
			ex.printStackTrace();
			
			throw ex;
		}
	}
}
