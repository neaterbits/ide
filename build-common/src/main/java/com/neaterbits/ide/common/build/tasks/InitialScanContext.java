package com.neaterbits.ide.common.build.tasks;

import java.util.Collection;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.language.CompilableLanguage;
import com.neaterbits.ide.common.resource.ModuleResourcePath;

public final class InitialScanContext extends TaskBuilderContext {

	public InitialScanContext(BuildRoot buildRoot, CompilableLanguage language) {
		super(buildRoot, language);
	}

	public InitialScanContext(TaskBuilderContext context) {
		super(context);
	}
	
	public Collection<ModuleResourcePath> getModules() {
		return buildRoot.getModules();
	}
}
