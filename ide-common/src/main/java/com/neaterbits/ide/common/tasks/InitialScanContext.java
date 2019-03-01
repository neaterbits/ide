package com.neaterbits.ide.common.tasks;


import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.tasks.TaskBuilderContext;
import com.neaterbits.ide.common.language.CompileableLanguage;

public final class InitialScanContext extends TaskBuilderContext {

	public InitialScanContext(BuildRoot buildRoot, CompileableLanguage language) {
		super(buildRoot, language);
	}

	public InitialScanContext(TaskBuilderContext context) {
		super(context);
	}
}
