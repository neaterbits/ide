package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogData;
import com.neaterbits.structuredlog.model.LogDataEntry;
import com.neaterbits.structuredlog.model.LogEntry;

public final class StructuredTargetExecutorLogger implements TargetExecutorLogger {

	private final Log log;

	public StructuredTargetExecutorLogger() {
		this.log = new Log();
	}
	
	private LogEntry addLogEntry(BuildEntity buildEntity, String message) {

		final LogEntry logEntry = new LogEntry(buildEntity.getPath(), message);
		
		if (log.getEntries() == null) {
			log.setEntries(new ArrayList<>());
		}
		
		log.getEntries().add(logEntry);
		
		return logEntry;
	}
	
	public Log getLog() {
		return log;
	}

	private static void addTargetLogState(LogEntry logEntry, String dataType, Collection<Target<?>> targets) {
		
		addCollectionLogState(
				logEntry,
				dataType,
				targets,
				BuildEntity::getPath,
				Target::getDebugString);
	}

	private static <T> void addCollectionLogState(
			LogEntry logEntry,
			String dataType,
			Collection<T> data,
			Function<T, List<String>> toPath,
			Function<T, String> toString) {

		final List<LogDataEntry> dataEntries = data.stream()
				.map(dataEntry -> new LogDataEntry(toPath.apply(dataEntry), toString.apply(dataEntry)))
				.collect(Collectors.toList());
		
		final LogData toExecute = new LogData(dataType, dataEntries);
		
		if (logEntry.getData() == null) {
			logEntry.setData(new ArrayList<>());
		}
		
		logEntry.getData().add(toExecute);
	}
	
	private static void addTargetLogState(LogEntry logEntry, TargetExecutorLogState logState) {

		addTargetLogState(logEntry, "toExecute", logState.getToExecuteTargets());
		addTargetLogState(logEntry, "scheduled", logState.getScheduledTargets());
		addTargetLogState(logEntry, "completed", logState.getCompletedTargets());
		addTargetLogState(logEntry, "failed", logState.getFailedTargets().keySet());
		
	}
	
	@Override
	public void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState) {

		final LogEntry logEntry = addLogEntry(target, "Schedule target " + target.getDebugString());

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onCollect(Target<?> target, List<Object> targetObjects, Object collected, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(target, "Collected " + collected);

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onAction(Target<?> target, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(target, "Executed action to build " + target.getDebugString());

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onComplete(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(
				target,
				(exception == null ? "Completed" : "Failed") 
					+ " target " + target.getDebugString());

		addTargetLogState(logEntry, logState);
	}

}
