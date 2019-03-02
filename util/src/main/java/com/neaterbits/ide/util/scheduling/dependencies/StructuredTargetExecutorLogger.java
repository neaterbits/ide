package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogData;
import com.neaterbits.structuredlog.model.LogEntry;

public final class StructuredTargetExecutorLogger implements TargetExecutorLogger {

	private final Log log;

	public StructuredTargetExecutorLogger() {
		this.log = new Log();
	}
	
	private static String makePath(BuildEntity buildEntity) {
		
		final List<String> list = buildEntity.getPath();
		
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < list.size(); ++ i) {
		
			if (i > 0) {
				sb.append("=>");
			}
			
			sb.append(list.get(i));
		}
		
		return sb.toString();
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
		
		addCollectionLogState(logEntry, dataType, targets, Target::getDebugString);
	}

	private static <T> void addCollectionLogState(LogEntry logEntry, String dataType, Collection<T> data, Function<T, String> toString) {

		final List<String> strings = data.stream()
				.map(toString)
				.collect(Collectors.toList());
		
		final LogData toExecute = new LogData(dataType, strings);
		
		if (logEntry.getData() == null) {
			logEntry.setData(new ArrayList<>());
		}
		
		logEntry.getData().add(toExecute);
	}
	
	@Override
	public void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState) {

		final LogEntry logEntry = addLogEntry(target, "Schedule target " + target.getDebugString());

		addTargetLogState(logEntry, "toExecute", logState.getToExecuteTargets());
		addTargetLogState(logEntry, "scheduled", logState.getScheduledTargets());
		addTargetLogState(logEntry, "completed", logState.getCompletedTargets());
		addTargetLogState(logEntry, "failed", logState.getFailedTargets().keySet());
	}

	@Override
	public void onCollect(Target<?> target, List<Object> targetObjects, Object collected, TargetExecutorLogState logState) {
		
	}

	@Override
	public void onAction(Target<?> target, TargetExecutorLogState logState) {
		
	}

	@Override
	public void onComplete(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
	}

}
