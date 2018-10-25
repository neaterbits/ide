package com.neaterbits.ide.util.scheduling.dependencies.builder;

import java.io.File;
import java.util.function.Function;

public interface PrerequisiteFromBuilder<CONTEXT extends TaskContext, FROM> {

	void withFile(Function<FROM, File> withFile);
	
}
