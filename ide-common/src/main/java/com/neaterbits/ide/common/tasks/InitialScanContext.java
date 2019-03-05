package com.neaterbits.ide.common.tasks;


import java.util.Objects;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.tasks.TaskBuilderContext;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.model.codemap.CodeMapGatherer;

public final class InitialScanContext extends TaskBuilderContext {

	private final CodeMapGatherer codeMapGatherer;
	
	public InitialScanContext(BuildRoot buildRoot, CompileableLanguage language, CodeMapGatherer codeMapGatherer) {
		super(buildRoot, language);
	
		Objects.requireNonNull(codeMapGatherer);
		
		this.codeMapGatherer = codeMapGatherer;
	}

	public InitialScanContext(TaskBuilderContext context, CodeMapGatherer codeMapGatherer) {
		super(context);

		Objects.requireNonNull(codeMapGatherer);
		
		this.codeMapGatherer = codeMapGatherer;
	}

	public CodeMapGatherer getCodeMapGatherer() {
		return codeMapGatherer;
	}
}
