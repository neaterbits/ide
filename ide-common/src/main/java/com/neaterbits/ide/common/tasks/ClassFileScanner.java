package com.neaterbits.ide.common.tasks;

import java.io.File;

public interface ClassFileScanner {

	void addClassFileToCodeMap(File classFile);
	
	void addLibraryFileToCodeMap(File compiledModuleFile);
}
