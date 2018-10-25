package com.neaterbits.ide.common.build.compile;

import java.util.List;

import com.neaterbits.ide.common.build.model.compile.BuildIssue;

public final class CompilerStatus {

	private final boolean executedOk;
	private final List<BuildIssue> issues;

	public CompilerStatus(boolean executedOk, List<BuildIssue> issues) {
		this.executedOk = executedOk;
		this.issues = issues;
	}

	public boolean executedOk() {
		return executedOk;
	}

	public List<BuildIssue> getIssues() {
		return issues;
	}
}
