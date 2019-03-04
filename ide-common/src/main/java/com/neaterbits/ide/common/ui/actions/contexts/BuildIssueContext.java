package com.neaterbits.ide.common.ui.actions.contexts;

import java.util.Objects;

import com.neaterbits.ide.common.build.model.compile.BuildIssue;

public final class BuildIssueContext extends ActionContext {

	private final BuildIssue buildIssue;

	public BuildIssueContext(BuildIssue buildIssue) {

		Objects.requireNonNull(buildIssue);
		
		this.buildIssue = buildIssue;
	}

	public BuildIssue getBuildIssue() {
		return buildIssue;
	}
}
