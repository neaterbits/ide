package com.neaterbits.ide.common.model.common;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.common.model.ResolvedTypes;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.component.common.language.LanguageComponent;

public class SourceFileInfo {

	private final SourceFileResourcePath path;
	private final LanguageComponent language;
	private final ResolvedTypes resolvedTypes; // for parser resolving of referenced types
	
	public SourceFileInfo(SourceFileResourcePath path, LanguageComponent language, ResolvedTypes resolvedTypes) {

		Objects.requireNonNull(path);
		Objects.requireNonNull(language);
		Objects.requireNonNull(resolvedTypes);
		
		this.path = path;
		this.language = language;
		this.resolvedTypes = resolvedTypes;
	}

	public SourceFileResourcePath getPath() {
		return path;
	}

	public LanguageComponent getLanguage() {
		return language;
	}

	public ResolvedTypes getResolvedTypes() {
		return resolvedTypes;
	}

	public File getFile() {
		return path.getFile();
	}
}
