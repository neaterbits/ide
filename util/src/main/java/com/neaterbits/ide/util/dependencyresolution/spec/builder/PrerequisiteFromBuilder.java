package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.io.File;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

public interface PrerequisiteFromBuilder<CONTEXT extends TaskContext, FROM> {

	void withFile(Function<FROM, File> withFile);
	
}
