package com.neaterbits.structuredlog.binary.model;

import java.util.Objects;

public abstract class LogField extends LogNode {

	private final String fieldName;

	LogField(int sequenceNo, LogObject parent, String fieldName) {
		super(sequenceNo, parent);
		
		Objects.requireNonNull(fieldName);
		
		this.fieldName = fieldName;
	}

	public final String getFieldName() {
		return fieldName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogField other = (LogField) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}
}
