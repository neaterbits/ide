package com.neaterbits.ide.core.ui.actions.contexts;

import java.util.Objects;

import com.neaterbits.build.types.compile.BuildIssue;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

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
