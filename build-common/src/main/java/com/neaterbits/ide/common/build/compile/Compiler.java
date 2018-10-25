package com.neaterbits.ide.common.build.compile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public interface Compiler {

	boolean supportsCompilingMultipleFiles();
	
	CompilerStatus compile(List<SourceFileResourcePath> sourceFiles, File targetDirectory, List<File> compiledDependencies) throws IOException;
	
}
