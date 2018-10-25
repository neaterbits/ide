package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.ide.buildsystem.maven.elements.MavenPlugin;

final class StackBuild extends StackBase implements PluginsSetter {

	private List<MavenPlugin> plugins;
	
	StackBuild(Context context) {
		super(context);

		this.plugins = new ArrayList<>();
	}

	List<MavenPlugin> getPlugins() {
		return plugins;
	}

	@Override
	public void setPlugins(List<MavenPlugin> plugins) {
		this.plugins = plugins;
	}
}
