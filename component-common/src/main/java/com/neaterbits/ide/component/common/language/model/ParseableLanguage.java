package com.neaterbits.ide.component.common.language.model;

import com.neaterbits.compiler.util.model.ResolvedTypes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface ParseableLanguage {

	Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes) throws IOException;
	
	SourceFileModel parseFile(String string, ResolvedTypes resolvedTypes);
	
}
