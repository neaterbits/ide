package com.neaterbits.ide.util.dependencyresolution;

import java.util.Objects;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

final class TargetState<CONTEXT extends TaskContext> extends TargetStateMachine<CONTEXT> {

	private final Target<?> target;
	
	// private Status status;
	private Exception exception;
	
	TargetState(Target<?> target) {
		super(target);
		
		Objects.requireNonNull(target);
		
		this.target = target;
		
		// this.status = Status.TO_EXECUTE;
	}
	
	Target<?> getTarget() {
		return target;
	}

	Exception getException() {
		return exception;
	}

	Status getStatus() {
		return getCurState().getStatus();
	}

	@Override
	public String toString() {
		return "TargetState [target=" + target.getDebugString() + ", state=" + getCurStateName() + "]";
	}

	/*
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

	void moveTargetFromToScheduledToActionPerformedCollect() {
		
		if (status != Status.SCHEDULED) {
			throw new IllegalStateException();
		}
		
		setStatus(Status.ACTION_PERFORMED_COLLECTING_SUBTARGETS);
	}

	void moveTargetFromActionPerformedCollectToComplete() {
		
		if (status != Status.ACTION_PERFORMED_COLLECTING_SUBTARGETS) {
			throw new IllegalStateException();
		}
		
		setStatus(Status.SUCCESS);
	}

	void moveTargetFromActionPerformedCollectToFailed() {
		
		if (status != Status.ACTION_PERFORMED_COLLECTING_SUBTARGETS) {
			throw new IllegalStateException();
		}
		
		setStatus(Status.FAILED);
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
	*/
}
