package com.neaterbits.ide.util.scheduling.dependencies;

final class ActionResult {
	final Object result;
	final Exception exception;

	public ActionResult(Object result, Exception exception) {
		this.result = result;
		this.exception = exception;
	}
}
