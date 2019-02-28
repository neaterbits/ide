package com.neaterbits.ide.buildmain;

import java.io.File;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.build.tasks.ModulesBuildContext;
import com.neaterbits.ide.common.build.tasks.TargetBuilderModules;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.component.java.language.JavaCompileableLanguage;
import com.neaterbits.ide.component.java.language.JavaCompiler;

public class BuildMain {

	private static void usage() {
		System.err.println("usage: <projectdir>");
	}
	
	public static void main(String [] args) throws ScanException {
	
		if (args.length != 1) {
			usage();
		}
		else {
			final String projectDirString = args[0];
			final File projectDir = new File(projectDirString);
			
			if (!projectDir.isDirectory()) {
				usage();
			}
			else {
				final BuildSystems buildSystems = new BuildSystems(); 
				final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);
				final BuildRoot buildRoot = new BuildRootImpl<>(projectDir, buildSystem.scan(projectDir));
				
				System.out.println("Modules to build:");
				
				for (ModuleResourcePath module : buildRoot.getModules()) {
					System.out.println(module.getName());
				}

				final TargetBuilderModules targetBuilderModules = new TargetBuilderModules();
				
				final ModulesBuildContext context = new ModulesBuildContext(
						buildRoot,
						new JavaCompileableLanguage(),
						new JavaCompiler(),
						null);

				targetBuilderModules.execute(context);
			}
		}
	}
}
