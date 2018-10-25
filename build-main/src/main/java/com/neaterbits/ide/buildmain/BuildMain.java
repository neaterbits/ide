package com.neaterbits.ide.buildmain;

import java.io.File;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.build.tasks.ModulesBuildContext;
import com.neaterbits.ide.common.build.tasks.TargetBuilderModules;
import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.component.java.language.JavaCompilableLanguage;
import com.neaterbits.ide.component.java.language.JavaCompiler;
import com.neaterbits.ide.util.scheduling.dependencies.PrintlnTargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.dependencies.TargetExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.TargetFinder;
import com.neaterbits.ide.util.scheduling.dependencies.TargetFinderLogger;
import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;

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

				final TargetSpec<ModulesBuildContext, ?, ?> targetSpec = TargetBuilderModules.makeTargetBuilderModules();
				
				final TargetFinder targetFinder = new TargetFinder();
				
				final ModulesBuildContext context = new ModulesBuildContext(
						buildRoot,
						new JavaCompilableLanguage(),
						new JavaCompiler(),
						null);
				
				final TargetFinderLogger targetFinderLogger = null; // new PrintlnTargetFinderLogger();
				
				targetFinder.computeTargets(targetSpec, context, targetFinderLogger, target -> {
					
					target.printTargets();
					
					final TargetExecutor targetExecutor = new TargetExecutor();
					
					targetExecutor.runTargets(context, target, new PrintlnTargetExecutorLogger());
				});
			}
		}
	}
}
