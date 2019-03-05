package com.neaterbits.ide.common.language;

import java.io.IOException;
import java.util.Set;

import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledFileResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;

public interface CompileableLanguage {
	
	CompleteName getCompleteName(SourceFileResourcePath sourceFile);
	
	Set<TypeName> getTypesFromCompiledModuleFile(CompiledModuleFileResourcePath compiledModuleFileResourcePath) throws IOException;

	Set<TypeName> getTypesFromLibraryFile(LibraryResourcePath libraryResourcePath) throws IOException;
	
	CompiledFileResourcePath getCompiledFilePath(TargetDirectoryResourcePath targetDirectory, SourceFileResourcePath sourceFile);

	boolean canReadCodeMapFromCompiledCode();
	
}
