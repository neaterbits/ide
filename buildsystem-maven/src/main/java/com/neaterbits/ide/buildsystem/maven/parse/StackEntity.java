package com.neaterbits.ide.buildsystem.maven.parse;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

abstract class StackEntity extends StackBase implements EntitySetter {
	
	private String groupId;
	private String artifactId;
	private String version;
	private String packaging;

	StackEntity(Context context) {
		super(context);
	}

	String getGroupId() {
		return groupId;
	}
	
	@Override
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getArtifactId() {
		return artifactId;
	}
	
	@Override
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public String getVersion() {
		return version;
	}
	
	@Override
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getPackaging() {
		return packaging;
	}
	
	@Override
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}
	
	final MavenModuleId makeModuleId() {
		return new MavenModuleId(groupId, artifactId, version);
	}
}
