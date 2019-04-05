package com.neaterbits.ide.util.dependencyresolution;

final class Result {
	final Object result;
	final Exception exception;

	public Result(Object result, Exception exception) {
		this.result = result;
		this.exception = exception;
	}
}
