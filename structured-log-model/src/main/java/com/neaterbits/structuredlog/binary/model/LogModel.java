package com.neaterbits.structuredlog.binary.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class LogModel {

	private final Map<Integer, String> typeNames;
	private final Map<Integer, String> fieldNames;

	private final List<Integer> logRoots;
	
	private final Map<Integer, LogObject> objectsByConstructorLogSequenceNo;
	private final List<LogObject> logRootObjects;
	
	public LogModel(
			Map<Integer, String> typeNames,
			Map<Integer, String> fieldNames,
			Map<Integer, LogObject> objectsByConstructorSequenceNo,

			List<Integer> logRoots,
			
			List<LogObject> logRootObjects) {

		if (logRoots.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (logRootObjects.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.typeNames = Collections.unmodifiableMap(typeNames);
		this.fieldNames = Collections.unmodifiableMap(fieldNames);
		this.objectsByConstructorLogSequenceNo = Collections.unmodifiableMap(objectsByConstructorSequenceNo);
		this.logRoots = Collections.unmodifiableList(logRoots);
		this.logRootObjects = Collections.unmodifiableList(logRootObjects);
	}

	public List<Integer> getLogRoots() {
		return logRoots;
	}
		
	public List<LogObject> getLogRootObjects() {
		return logRootObjects;
	}

	public Collection<LogObject> getLogObjects() {
		return objectsByConstructorLogSequenceNo.values();
	}

	public String getTypeName(int typeId) {
		return typeNames.get(typeId);
	}
	
	public String getFieldName(int fieldIdx) {
		return fieldNames.get(fieldIdx);
	}

	public LogObject getLogObject(int constructorLogSequenceNo) {
		
		return objectsByConstructorLogSequenceNo.get(constructorLogSequenceNo);
	}
}
