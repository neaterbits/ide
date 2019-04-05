package com.neaterbits.ide.util.dependencyresolution.spec.builder;

import java.io.File;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.ide.util.scheduling.task.TaskContext;

class PrerequisiteFromBuilderImpl<CONTEXT extends TaskContext, TARGET> 
		implements PrerequisiteFromBuilder<CONTEXT, TARGET> {

	private final String description;
	private Function<TARGET, File> file;
	
	PrerequisiteFromBuilderImpl(String description) {
		
		Objects.requireNonNull(description);
		
		this.description = description;
	}

	@Override
	public void withFile(Function<TARGET, File> withFile) {

		if (this.file != null) {
			throw new IllegalStateException();
		}

		this.file = withFile;
	}

	final String getDescription() {
		return description;
	}
}
