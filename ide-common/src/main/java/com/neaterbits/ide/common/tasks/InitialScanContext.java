package com.neaterbits.ide.common.tasks;


import java.util.Objects;

import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.strategies.common.TaskBuilderContext;
import com.neaterbits.ide.common.model.codemap.CodeMapGatherer;

public final class InitialScanContext extends TaskBuilderContext {

	private final CompileableLanguage compileableLanguage;
	private final CodeMapGatherer codeMapGatherer;
	
	public InitialScanContext(BuildRoot buildRoot, CompileableLanguage language, CodeMapGatherer codeMapGatherer) {
		super(buildRoot, language);
	
		Objects.requireNonNull(codeMapGatherer);
		
		this.compileableLanguage = language;
		this.codeMapGatherer = codeMapGatherer;
	}

	public InitialScanContext(TaskBuilderContext context, CompileableLanguage language, CodeMapGatherer codeMapGatherer) {
		super(context);

		Objects.requireNonNull(codeMapGatherer);
		
		this.compileableLanguage = language;
		this.codeMapGatherer = codeMapGatherer;
	}

	public CompileableLanguage getCompileableLanguage() {
		return compileableLanguage;
	}

	public CodeMapGatherer getCodeMapGatherer() {
		return codeMapGatherer;
	}
}
