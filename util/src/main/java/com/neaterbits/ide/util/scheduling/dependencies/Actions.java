package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.neaterbits.ide.util.scheduling.dependencies.builder.ActionFunction;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;
import com.neaterbits.ide.util.scheduling.task.ProcessResult;

class Actions {
	
	static <CONTEXT extends TaskContext> void runOrScheduleAction(
			TargetExecutionContext<CONTEXT> context,
			Action<?> action,
			Target<?> target,
			BiConsumer<Exception, Boolean> onCompleted) {
		
		if (action.getConstraint() == null) {
			final Exception exception = performAction(context, action, target);
			
			onCompleted.accept(exception, false);
		}
		else {
			context.asyncExecutor.schedule(
					action.getConstraint(),
					null,
					param -> {
						return performAction(context, action, target);
					},
					(param, exception) -> {
						onCompleted.accept(exception, true);
					} );
		}
	}
	
	static <CONTEXT extends TaskContext> void runOrScheduleActionWithResult(
			TargetExecutionContext<CONTEXT> context,
			ActionWithResult<?> actionWithResult,
			Target<?> target,
			BiConsumer<Exception, Boolean> onCompleted) {
		
		if (actionWithResult.getConstraint() == null) {
			
			final ActionResult result = performActionWithResult(context, actionWithResult, target);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final ProcessResult<CONTEXT, Object, Object> processResult = (ProcessResult)actionWithResult.getOnResult();
			
			processResult.process(context.context, target.getTargetObject(), result.result);
			
			onCompleted.accept(result.exception, false);
		}
		else {
			context.asyncExecutor.schedule(
					actionWithResult.getConstraint(),
					null,
					param -> {
						return performActionWithResult(context, actionWithResult, target);
					},
					(param, result) -> {
						@SuppressWarnings({ "unchecked", "rawtypes" })
						final ProcessResult<CONTEXT, Object, Object> processResult = (ProcessResult)actionWithResult.getOnResult();
						
						processResult.process(context.context, target.getTargetObject(), result.result);
						
						onCompleted.accept(result.exception, true);
					});
		}
	}
	
	private static <CONTEXT extends TaskContext> Exception performAction(
			TargetExecutionContext<CONTEXT> context,
			Action<?> action,
			Target<?> target) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final ActionFunction<CONTEXT, Object> actionFunction = (ActionFunction)action.getActionFunction();
		
		if (context.logger != null) {
			context.logger.onAction(target, context.state);
		}
		
		try {
			actionFunction.perform(context.context, target.getTargetObject(), context.state);
			
		} catch (Exception ex) {
			return ex;
		}
		
		return null;
	}

	private static <CONTEXT extends TaskContext> ActionResult performActionWithResult(
			TargetExecutionContext<CONTEXT> context,
			ActionWithResult<?> action,
			Target<?> target) {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final BiFunction<CONTEXT, Object, Object> actionFunction = (BiFunction)action.getActionWithResult();
		
		if (context.logger != null) {
			context.logger.onAction(target, context.state);
		}

		Exception exception = null;
		Object result = null;
		
		try {
			result = actionFunction.apply(context.context, target.getTargetObject());
		}
		catch (Exception ex) {
			exception = ex;
		}

		return new ActionResult(result, exception);
	}
}
