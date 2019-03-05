package com.neaterbits.ide.common.tasks;


import java.util.Objects;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.tasks.TaskBuilderContext;
import com.neaterbits.ide.common.language.CompileableLanguage;

public final class InitialScanContext extends TaskBuilderContext {

	private final ClassFileScanner classFileScanner;
	
	public InitialScanContext(BuildRoot buildRoot, CompileableLanguage language, ClassFileScanner classFileScanner) {
		super(buildRoot, language);
	
		Objects.requireNonNull(classFileScanner);
		
		this.classFileScanner = classFileScanner;
	}

	public InitialScanContext(TaskBuilderContext context, ClassFileScanner classFileScanner) {
		super(context);

		Objects.requireNonNull(classFileScanner);
		
		this.classFileScanner = classFileScanner;
	}

	public ClassFileScanner getClassFileScanner() {
		return classFileScanner;
	}
}
