package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogData;
import com.neaterbits.structuredlog.model.LogDataEntry;
import com.neaterbits.structuredlog.model.LogEntry;
import com.neaterbits.structuredlog.model.LogPath;

public final class StructuredTargetExecutorLogger implements TargetExecutorLogger {

	private final Log log;

	private final Map<LogPath, Integer> paths;
	
	public StructuredTargetExecutorLogger() {
		this.log = new Log();
		this.paths = new HashMap<>();
	}
	
	private LogEntry addLogEntry(BuildEntity buildEntity, String message) {

		final LogEntry logEntry = new LogEntry(
				buildEntity != null ? makePath(buildEntity.getPath()) : null,
				message);
		
		if (log.getEntries() == null) {
			log.setEntries(new ArrayList<>());
		}
		
		log.getEntries().add(logEntry);
		
		return logEntry;
	}
	
	public Log makeLog() {
		return log;
	}

	private Integer makePath(List<String> list) {
		
		final LogPath logPath = new LogPath(list);
		
		if (log.getPaths() == null) {
			log.setPaths(new ArrayList<>());
		}
		
		Integer pathIndex = paths.get(logPath);
		
		if (pathIndex == null) {
			pathIndex = paths.size();
			
			paths.put(logPath, pathIndex);
			
			log.getPaths().add(logPath);
		}
		
		return pathIndex;
	}

	private void addTargetLogState(LogEntry logEntry, String dataType, Collection<Target<?>> targets) {
		
		addCollectionLogState(
				logEntry,
				dataType,
				targets,
				BuildEntity::getPath,
				Target::getDebugString);
	}

	private <T> void addCollectionLogState(
			LogEntry logEntry,
			String dataType,
			Collection<T> data,
			Function<T, List<String>> toPath,
			Function<T, String> toString) {

		final List<LogDataEntry> dataEntries = data.stream()
				.map(dataEntry -> new LogDataEntry(makePath(toPath.apply(dataEntry)), toString.apply(dataEntry)))
				.collect(Collectors.toList());
		
		final LogData toExecute = new LogData(dataType, dataEntries);
		
		if (logEntry.getData() == null) {
			logEntry.setData(new ArrayList<>());
		}
		
		logEntry.getData().add(toExecute);
	}
	
	private void addTargetLogState(LogEntry logEntry, TargetExecutorLogState logState) {

		addTargetLogState(logEntry, "toExecute", logState.getToExecuteTargets());
		addTargetLogState(logEntry, "scheduled", logState.getScheduledTargets());
		addTargetLogState(logEntry, "completed", logState.getCompletedTargets());
		addTargetLogState(logEntry, "failed", logState.getFailedTargets().keySet());
		
	}
	
	
	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(null, "Schedule more targets numScheduledJobs=" + numScheduledJobs);

		addTargetLogState(logEntry, logState);
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

		if (exception != null) {
			System.out.println("## exception class " + exception);
			exception.printStackTrace();
		}
		
		addTargetLogState(logEntry, logState);
	}

}
