package com.neaterbits.structuredlog.binary.logging;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class LogContext extends BaseBinaryLogWriter {

	private final Map<Class<?>, ClassLogInfo> classes;
	private final Map<String, Integer> fieldNames;

	private final ByteArrayOutputStream baos;
	private final DataOutput dataStream;
	
	@Override
	DataOutput getDataStream() {
		return dataStream;
	}

	public LogContext() {
		this.classes = new HashMap<>();
		this.fieldNames = new HashMap<>();
		
		this.baos = new ByteArrayOutputStream(100000);
		this.dataStream = new DataOutputStream(baos);
	}
	
	private int writeGetOrAllocateTypeId(Class<? extends Loggable> type) {
		
		Objects.requireNonNull(type);

		final int typeId;

		final ClassLogInfo logInfo = classes.get(type);

		if (logInfo != null) {
			typeId = logInfo.getLogTypeId();
			
			writeTypeId(typeId);
		}
		else {
			typeId = classes.size();
		
			classes.put(type, new ClassLogInfo(typeId));
			
			writeTypeId(typeId | 1 << 31);
			writeString(type.getName());
		}

		return typeId;
	}

	private int writeGetOrAllocateFieldName(String field) {
		
		Objects.requireNonNull(field);

		Integer fieldIdx = fieldNames.get(field);

		if (fieldIdx != null) {
			writeInteger(fieldIdx);
		}
		else {
			fieldIdx = fieldNames.size();
		
			fieldNames.put(field, fieldIdx);
			
			writeInteger(fieldIdx | 1 << 31);
			writeString(field);
		}

		return fieldIdx;
	}

	public void writeLogBufferToOutput(OutputStream outputStream) throws IOException {
		
		final byte [] data = baos.toByteArray();
		
		outputStream.write(data);
	}
	
	<T extends Loggable> T setLoggableField(Loggable loggable, String identifier, String field, T sub) {

		final int sequenceNo = writeHeader(LogCommand.SET_LOGGABLE_FIELD, identifier);
		
		debugWrite(sequenceNo, LogCommand.SET_LOGGABLE_FIELD, "identifier", identifier, "field", field, "sub", sub != null ? sub.toString() : null);
		
		writeGetOrAllocateFieldName(field);
		
		writeIdentifier(sub);
		
		return sub;
	}

	
	void logRootObject(int constructorLogSequenceNo, String identifier) {

		final int sequenceNo = writeLogCommand(LogCommand.ROOT_OBJECT);
		writeSequenceNo(constructorLogSequenceNo);
		writeIdentifier(identifier);

		debugWrite(sequenceNo, LogCommand.ROOT_OBJECT, "sequenceNo", String.valueOf(sequenceNo), "identifier", identifier);
	}

	int logConstructor(Class<? extends Loggable> type, String identifier, String localIdentifier, String description) {

		final int sequenceNo = writeLogCommand(LogCommand.CONSTRUCTOR);

		writeSequenceNo(sequenceNo);
		
		final int typeId = writeGetOrAllocateTypeId(type);

		debugWrite(
				sequenceNo,
				LogCommand.CONSTRUCTOR,
				"type", type.getName(),
				"typeId", String.valueOf(typeId),
				"identifier", identifier,
				"localIdentifier", localIdentifier,
				"description", description);

		writeIdentifier(identifier);
		writeIdentifier(localIdentifier);
		writeDescription(description);
		
		return sequenceNo;
	}

	
	void logConstructorScalarField(LogContext logContext, String identifier, String field, Object value) {
		
		
		final int sequenceNo = writeHeader(LogCommand.CONSTRUCTOR_SCALAR_FIELD, identifier);
		
		final int fieldIdx = writeGetOrAllocateFieldName(field);

		debugWrite(
				sequenceNo,
				LogCommand.CONSTRUCTOR_SCALAR_FIELD,
				"identifier", identifier,
				"field", field,
				"fieldIdx", String.valueOf(fieldIdx),
				"value", value != null ? value.toString() : null);

		writeScalar(value);
	}

	void logConstructorLoggableField(LogContext logContext, String identifier, String field, Loggable value) {
		
		if (value != null) {
			final int sequenceNo = writeHeader(LogCommand.CONSTRUCTOR_LOGGABLE_FIELD, identifier);
			
			final int fieldIdx = writeGetOrAllocateFieldName(field);

			debugWrite(
					sequenceNo,
					LogCommand.CONSTRUCTOR_LOGGABLE_FIELD,
					"identifier", identifier,
					"field", field,
					"fieldIdx", String.valueOf(fieldIdx),
					"value", value != null ? value.getLogDebugString() : null);

			writeLoggableReference(value);
		}
	}

	<T extends Loggable> Collection<T> logConstructorCollectionField(LogContext logContext, String identifier, String field, Collection<T> values) {

		writeConstructorCollectionField(identifier, field, values);
		
		return new CollectionWrapper<>(values);
	}

	<T extends Loggable> List<T> logConstructorListField(LogContext logContext, String identifier, String field, List<T> values) {

		writeConstructorCollectionField(identifier, field, values);
		
		return new ListWrapper<>(values);
	}
	
	private void writeConstructorCollectionField(String identifier, String field, Collection<? extends Loggable> values) {
		
		final int sequenceNo = writeHeader(LogCommand.CONSTRUCTOR_COLLECTION_FIELD, identifier);
		
		final int fieldIdx = writeGetOrAllocateFieldName(field);
		
		debugWrite(
				sequenceNo,
				LogCommand.CONSTRUCTOR_COLLECTION_FIELD,
				"identifier", identifier,
				"field", field,
				"fieldIdx", String.valueOf(fieldIdx),
				"numEntries", values != null ? String.valueOf(values.size()) : null);

		writeCollection(values);
	}
	

	void logSetScalarField(LogContext logContext, String identifier, String field, Object value) {
		
		final int sequenceNo = writeHeader(LogCommand.SET_SCALAR_FIELD, identifier);
		
		final int fieldIdx = writeGetOrAllocateFieldName(field);

		debugWrite(
				sequenceNo,
				LogCommand.SET_SCALAR_FIELD,
				"identifier", identifier,
				"field", field,
				"fieldIdx", String.valueOf(fieldIdx),
				"value", value != null ? value.toString() : null);

		writeScalar(value);
	}

	
	void logSetLoggableField(LogContext logContext, String identifier, String field, Loggable value) {
		
		final int sequenceNo = writeHeader(LogCommand.SET_LOGGABLE_FIELD, identifier);
		
		final int fieldIdx = writeGetOrAllocateFieldName(field);

		debugWrite(
				sequenceNo,
				LogCommand.SET_LOGGABLE_FIELD,
				"identifier", identifier,
				"field", field,
				"fieldIdx", String.valueOf(fieldIdx),
				"value", value != null ? value.getLogDebugString() : null);

		writeIdentifier(value);
	}
	
	<T extends Loggable> Collection<T> logSetCollectionField(Loggable loggable, String field, Collection<T> collection) {
		
		writeSetCollectionField(loggable, field, collection);
		
		return new CollectionWrapper<>(collection);
	}

	<T extends Loggable> List<T> logSetListField(Loggable loggable, String field, List<T> list) {

		writeSetCollectionField(loggable, field, list);
		
		return new ListWrapper<>(list);
	}
	
	private void writeSetCollectionField(Loggable loggable, String field, Collection<? extends Loggable> collection) {

		final int sequenceNo = writeHeader(LogCommand.SET_COLLECTION_FIELD, loggable);
		
		final int fieldIdx = writeGetOrAllocateFieldName(field);

		debugWrite(
				sequenceNo,
				LogCommand.SET_COLLECTION_FIELD,
				"field", field,
				"fieldIdx", String.valueOf(fieldIdx),
				"numEntries", collection != null ? String.valueOf(collection.size()) : null);

		writeCollection(collection);
	}
	
	
	void error(Loggable loggable, String message) {
		final int sequenceNo = writeLogCommand(LogCommand.ERROR);

		debugWrite(sequenceNo, LogCommand.ERROR, "message", message);

		writeString(message);
	}

	void debug(Loggable loggable, String message) {
		final int sequenceNo = writeLogCommand(LogCommand.DEBUG);

		debugWrite(sequenceNo, LogCommand.DEBUG, "message", message);

		writeString(message);
	}
	
	void trace(Loggable loggable, String message) {
		final int sequenceNo = writeLogCommand(LogCommand.TRACE);
		
		debugWrite(sequenceNo, LogCommand.TRACE, "message", message);

		writeString(message);
	}
	
	private void writeCollection(Collection<? extends Loggable> values) {
		try {
			if (values == null || values.isEmpty()) {
				dataStream.writeInt(0);
			}
			else {
				dataStream.writeInt(values.size());
				
				for (Loggable loggable : values) {
					writeLoggableReference(loggable);
				}
			}
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private void writeLoggableReference(Loggable loggable) {
		writeSequenceNo(loggable.getConstructorLogSequenceNo());
	}
}
