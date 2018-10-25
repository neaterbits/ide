package com.neaterbits.ide.common.buildsystem;

import java.io.File;

public interface BuildSystem {

	boolean isBuildSystemFor(File rootDirectory);
	
	<MODULE_ID, PROJECT, DEPENDENCY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> scan(File rootDirectory) throws ScanException;
}
