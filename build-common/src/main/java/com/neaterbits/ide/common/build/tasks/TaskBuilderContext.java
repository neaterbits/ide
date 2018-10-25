package com.neaterbits.ide.common.build.tasks;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.language.CompilableLanguage;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

abstract class TaskBuilderContext extends TaskContext {
	final BuildRoot buildRoot;
	final CompilableLanguage language;

	TaskBuilderContext(BuildRoot buildRoot, CompilableLanguage language) {
		this.buildRoot = buildRoot;
		this.language = language;
	}
	
	TaskBuilderContext(TaskBuilderContext context) {
		this(context.buildRoot, context.language);
	}
}
