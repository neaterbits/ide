package com.neaterbits.structuredlog.binary.model;

public final class ScalarLogObjectField extends LogField {

	private Object value;

	public ScalarLogObjectField(int sequenceNo, LogObject parent, String fieldName, Object value) {
		super(sequenceNo, parent, fieldName);

		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	void setValue(Object value) {
		this.value = value;
	}
}
