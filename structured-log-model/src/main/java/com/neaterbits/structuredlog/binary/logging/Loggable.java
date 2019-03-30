package com.neaterbits.structuredlog.binary.logging;

import java.util.Collection;
import java.util.List;

public interface Loggable {

	default void logRootObject(LogContext logContext) {
		logContext.logRootObject(getConstructorLogSequenceNo(), getLogIdentifier());
	}

	default int logConstructor(LogContext logContext, Class<? extends Loggable> type, String identifier, String localIdentifier, String description) {
		return logContext.logConstructor(type, identifier, localIdentifier, description);
	}
	
	default <T> T logConstructorScalarField(LogContext logContext, String identifier, String field, T value) {
		
		logContext.logConstructorScalarField(logContext, identifier, field, value);
		
		return value;
	}

	default <T extends Loggable> T logConstructorLoggableField(LogContext logContext, String identifier, String field, T value) {
		
		logContext.logConstructorLoggableField(logContext, identifier, field, value);
		
		return value;
	}

	default <T extends Loggable>
	Collection<T> logConstructorCollectionField(LogContext logContext, String identifier, String field, Collection<T> values) {
		
		return logContext.logConstructorCollectionField(logContext, identifier, field, values);
	}

	default <T extends Loggable>
	List<T> logConstructorListField(LogContext logContext, String identifier, String field, List<T> values) {
		
		return logContext.logConstructorListField(logContext, identifier, field, values);
	}

	default void debug(LogContext logContext, String message) {
		logContext.debug(this, message);
	}

	default <T extends Loggable> T logSetLoggableField(LogContext logContext, String identifier, String field, T value) {
		return logContext.setLoggableField(this, identifier, field, value);
	}

	default <T extends Loggable> Collection<T> logSetCollectionField(LogContext logContext, String identifier, String field, Collection<T> collection) {
		return logContext.logSetCollectionField(this, field, collection);
	}

	default <T extends Loggable> List<T> logSetListField(LogContext logContext, String field, List<T> list) {
		return logContext.logSetListField(this, field, list);
	}
	
	default String getLogDebugString() {
		
		final String text;
		
		if (getLogLocalIdentifier() != null) {
			text = getLogLocalIdentifier();
		}
		else if (getLogIdentifier() != null) {
			text = getLogIdentifier();
		}
		else if (getDescription() != null) {
			text = getDescription();
		}
		else{
			text = toString();
		}
		
		return text;
	}
	
	int getConstructorLogSequenceNo();
	
	String getLogIdentifier();
	
	String getLogLocalIdentifier();
	
	String getDescription();
}
