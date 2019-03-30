package com.neaterbits.structuredlog.binary.model;

import java.util.Objects;

public abstract class LogNode {

	private final int sequenceNo;
	
	private LogNode parent;

	public LogNode(int sequenceNo, LogNode parent) {
		this.sequenceNo = sequenceNo;
		this.parent = parent;
	}

	public final int getSequenceNo() {
		return sequenceNo;
	}
	
	public final LogNode getParent() {
		return parent;
	}
	
	final void setParentNode(LogNode node) {
		Objects.requireNonNull(node);
		
		if (this.parent != null) {
			throw new IllegalStateException("Already set");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sequenceNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogNode other = (LogNode) obj;
		if (sequenceNo != other.sequenceNo)
			return false;
		return true;
	}
}
