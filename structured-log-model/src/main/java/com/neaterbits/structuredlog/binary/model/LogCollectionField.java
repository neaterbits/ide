package com.neaterbits.structuredlog.binary.model;

import java.util.ArrayList;
import java.util.Collection;

public class LogCollectionField extends LogField {

	private Collection<LogObject> collection;
	
	public LogCollectionField(int sequenceNo, LogObject parent, String fieldName) {
		super(sequenceNo, parent, fieldName);
	}

	public LogCollectionField(int sequenceNo, LogObject parent, String fieldName, Collection<LogObject> collection) {
		super(sequenceNo, parent, fieldName);
		
		this.collection = new ArrayList<>(collection);
		
		collection.forEach(logObject -> logObject.setParent(this));
	}

	public Collection<LogObject> getCollection() {
		return collection;
	}

	@Override
	public String toString() {
		return "LogCollectionField [getSequenceNo()=" + getSequenceNo() + ", collection=" + collection + "]";
	}
	
	
}
