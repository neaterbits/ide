package com.neaterbits.ide.common.build.tasks;

import com.neaterbits.ide.common.build.compile.Compiler;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.compile.FileDependencyMap;
import com.neaterbits.ide.common.language.CompileableLanguage;

public final class ModulesBuildContext extends TaskBuilderContext {
	final Compiler compiler;
	private final FileDependencyMap fileDependencyMap;
	
	public ModulesBuildContext(
			BuildRoot buildRoot,
			
			CompileableLanguage language,

			Compiler compiler,
			
			FileDependencyMap fileDependencyMap) {
		
		super(buildRoot, language);
		
		this.compiler = compiler;
		this.fileDependencyMap = fileDependencyMap;
	}

	public ModulesBuildContext(ModulesBuildContext context) {
		super(context);
		
		this.compiler = context.compiler;
		this.fileDependencyMap = context.fileDependencyMap;
	}
}
