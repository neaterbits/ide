package com.neaterbits.ide.common.resource;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.util.modules.ModuleId;

public final class ModuleResource extends FileSystemResource {

	private final ModuleId moduleId;
	
	public ModuleResource(ModuleId moduleId, File file) {
		super(file, moduleId.getId());
	
		Objects.requireNonNull(moduleId);
		
		this.moduleId = moduleId;
	}

	public ModuleId getModuleId() {
		return moduleId;
	}
}
