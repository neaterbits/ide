package com.neaterbits.ide.util.scheduling.dependencies.builder;

public interface NoTargetTargetBuildSpecPrerequisitesBuilder {

	default void withNamedPrerequisite(String ... prerequisite) {
		withNamedPrerequisites(prerequisite);
	}

	void withNamedPrerequisites(String ... prerequisites);
	
}
