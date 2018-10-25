package com.neaterbits.ide.buildsystem.maven.elements;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

public class MavenDependency extends MavenEntity {

	public MavenDependency(MavenModuleId moduleId, String packaging) {
		super(moduleId, packaging);
	}
}
