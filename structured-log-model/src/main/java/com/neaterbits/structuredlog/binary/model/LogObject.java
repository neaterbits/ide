package com.neaterbits.structuredlog.binary.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.structuredlog.binary.logging.Loggable;


public final class LogObject extends LogNode implements Loggable {

	private final String type;
	private final String identifier;
	private final String localIdentifier;
	private final String description;
	
	private Map<String, LogField> fields;
	
	public LogObject(int sequenceNo, LogField parent, String type, String identifier, String localIdentifier, String description) {
		super(sequenceNo, parent);

		this.type = type;
		this.identifier = identifier;
		this.localIdentifier = localIdentifier;
		this.description = description;
	}

	void setParent(LogField field) {
		
		Objects.requireNonNull(field);
	
		super.setParentNode(field);
	}
	
	void addField(String fieldName, LogField field) {
		
		Objects.requireNonNull(fieldName);
		Objects.requireNonNull(field);
		
		if (fields == null) {
			this.fields = new HashMap<>();
		}
		else {
			if (fields.containsKey(fieldName)) {
				throw new IllegalArgumentException();
			}
		}
		
		fields.put(fieldName, field);
	}
	
	public String getType() {
		return type;
	}

	public String getSimpleType() {
		final int index = type.lastIndexOf('.');
		
		return type.substring(index + 1);
	}
	
	@Override
	public int getConstructorLogSequenceNo() {
		return getSequenceNo();
	}

	@Override
	public String getLogIdentifier() {
		return identifier;
	}

	@Override
	public String getLogLocalIdentifier() {
		return localIdentifier;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Collection<LogField> getFields() {
		return fields != null ? fields.values() : null;
	}

	@Override
	public String toString() {
		return "LogObject [type=" + type + ", identifier=" + identifier + ", localIdentifier=" + localIdentifier +", description=" + description
				+ ", fields=" + (fields != null ? fields.keySet() : null) + "]";
	}	
}
