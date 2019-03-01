package com.neaterbits.ide.common.build.tasks;

import java.util.Collection;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.util.scheduling.dependencies.builder.TaskContext;

public abstract class TaskBuilderContext extends TaskContext {
	private final BuildRoot buildRoot;
	private final CompileableLanguage language;

	protected TaskBuilderContext(BuildRoot buildRoot, CompileableLanguage language) {
		this.buildRoot = buildRoot;
		this.language = language;
	}
	
	protected TaskBuilderContext(TaskBuilderContext context) {
		this(context.buildRoot, context.language);
	}

	public final BuildRoot getBuildRoot() {
		return buildRoot;
	}

	public final CompileableLanguage getLanguage() {
		return language;
	}

	public final Collection<ModuleResourcePath> getModules() {
		return buildRoot.getModules();
	}

	public final CompiledModuleFileResourcePath getCompiledModuleFile(ModuleResourcePath module) {
		return buildRoot.getCompiledModuleFile(module);
	}
}
