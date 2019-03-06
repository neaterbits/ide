package com.neaterbits.ide.util.scheduling.dependencies;

import java.util.Objects;

final class TargetState {

	private Status status;
	private Exception exception;
	
	
	TargetState() {
		this.status = Status.TO_EXECUTE;
	}

	Exception getException() {
		return exception;
	}

	Status getStatus() {
		return status;
	}

	private void setStatus(Status status) {

		Objects.requireNonNull(status);
		
		if (this.status == status) {
			throw new IllegalStateException();
		}
		
		this.status = status;
	}
	
	void moveTargetFromToExecuteToScheduled() {
		
		if (status != Status.TO_EXECUTE) {
			throw new IllegalStateException();
		}
	
		setStatus(Status.SCHEDULED);
	}

	void moveTargetFromToExecuteToFailed() {
		
		if (status != Status.TO_EXECUTE) {
			throw new IllegalStateException();
		}
		
		setStatus(Status.FAILED);
	}

	void moveTargetFromScheduledToSuccess() {
		
		if (status != Status.SCHEDULED) {
			throw new IllegalStateException("status=" + status);
		}
		
		setStatus(Status.SUCCESS);
	}

	void moveTargetFromScheduledToFailed(Exception exception) {
		
		Objects.requireNonNull(exception);
		
		if (status != Status.SCHEDULED) {
			throw new IllegalStateException("state=" + status);
		}
		
		setStatus(Status.FAILED);
		
		this.exception = exception;
	}
}
