package com.neaterbits.ide.util.dependencyresolution.executor.logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.ide.util.dependencyresolution.builder.ActionLog;
import com.neaterbits.ide.util.dependencyresolution.executor.BuildEntity;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedObject;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProduct;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedProducts;
import com.neaterbits.ide.util.dependencyresolution.executor.CollectedTargetObjects;
import com.neaterbits.ide.util.dependencyresolution.executor.Status;
import com.neaterbits.ide.util.dependencyresolution.executor.Target;
import com.neaterbits.structuredlog.xml.model.Log;
import com.neaterbits.structuredlog.xml.model.LogData;
import com.neaterbits.structuredlog.xml.model.LogDataEntry;
import com.neaterbits.structuredlog.xml.model.LogEntry;
import com.neaterbits.structuredlog.xml.model.LogPath;

public final class StructuredTargetExecutorLogger implements TargetExecutorLogger {

	private final Log log;

	private final Map<LogPath, Integer> paths;
	
	public StructuredTargetExecutorLogger() {
		this.log = new Log();
		this.paths = new HashMap<>();
	}
	
	private LogEntry addLogEntry(BuildEntity buildEntity, Object entityObj, String state, String message) {

		final List<String> path;
		
		if (buildEntity != null) {
			path = buildEntity.getPath();
		
			path.add(String.format("%08x", System.identityHashCode(buildEntity)));
			
			if (entityObj != null) {
				path.add(String.format("%08x", System.identityHashCode(entityObj)));
			}
			
			if (state != null) {
				path.add(state);
			}
		}
		else {
			path = null;
		}
		
		final LogEntry logEntry = new LogEntry(
				path != null ? makePath(path) : null,
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
		addTargetLogState(logEntry, "collect", logState.getActionPerformedCollectTargets());
		addTargetLogState(logEntry, "completed", logState.getCompletedTargets());
		addTargetLogState(logEntry, "failed", logState.getFailedTargets().keySet());
		
	}
	
	
	@Override
	public void onScheduleTargets(int numScheduledJobs, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(null, null, null, "Schedule more targets numScheduledJobs=" + numScheduledJobs);

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onScheduleTarget(Target<?> target, Status hasCompletedPrerequisites, TargetExecutorLogState logState) {

		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				"Schedule target " + target.getDebugString() + " with prerequisites status " + hasCompletedPrerequisites);

		addTargetLogState(logEntry, logState);
	}

	private String getCollectedLogString(Object collected) {
		
		final String string;
		
		if (collected instanceof CollectedObject) {
			final CollectedObject collectedObject = (CollectedObject)collected;
			
			string = collectedObject.getClass().getSimpleName() + " " + collectedObject.getName() + " " + collectedObject.getCollected();
		}
		else if (collected != null) {
			string = collected.toString();
		}
		else {
			string = "null";
		}
		
		return string;
	}
	
	
	@Override
	public void onCollectProducts(Target<?> target, CollectedProducts subProducts, CollectedProduct collected,
			TargetExecutorLogState logState) {

		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				"Collected " + getCollectedLogString(collected.getProductObject()));

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onCollectTargetObjects(Target<?> target, CollectedTargetObjects targetObjects,
			CollectedProduct collected, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				"Collected targets " + getCollectedLogString(collected.getProductObject()));

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onActionCompleted(Target<?> target, TargetExecutorLogState logState, ActionLog actionLog) {
		
		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				"Executed action to build " + target.getDebugString()
			+ (actionLog != null ? ": " + actionLog.getCommandLine() : ""));

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onActionException(Target<?> target, TargetExecutorLogState logState, Exception exception) {

		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				"Failed build action on " + target.getDebugString()
			+ " " + exception.getClass().getName());

		addTargetLogState(logEntry, logState);
	}

	@Override
	public void onTargetDone(Target<?> target, Exception exception, TargetExecutorLogState logState) {
		
		final LogEntry logEntry = addLogEntry(
				target,
				target.getTargetObject(),
				logState.getTargetStatus(target).name(),
				(exception == null ? "Completed" : "Failed") 
					+ " target "
							+ (target.getTargetObject() != null ? target.getTargetObject().getClass().getSimpleName() + " " : "" )
							+ target.getDebugString());

		addTargetLogState(logEntry, logState);
	}
}
