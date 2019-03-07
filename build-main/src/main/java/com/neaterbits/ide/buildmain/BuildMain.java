package com.neaterbits.ide.buildmain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.build.tasks.ModulesBuildContext;
import com.neaterbits.ide.common.build.tasks.TargetBuilderModules;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.component.java.language.JavaLanguage;
import com.neaterbits.ide.component.java.language.JavaCompiler;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.StructuredTargetExecutorLogger;
import com.neaterbits.structuredlog.model.Log;
import com.neaterbits.structuredlog.model.LogIO;

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
				
				for (ProjectModuleResourcePath module : buildRoot.getModules()) {
					System.out.println(module.getName());
				}

				final TargetBuilderModules targetBuilderModules = new TargetBuilderModules();
				
				final ModulesBuildContext context = new ModulesBuildContext(
						buildRoot,
						new JavaLanguage(),
						new JavaCompiler(),
						null);

				// final TargetExecutorLogger logger = new PrintlnTargetExecutorLogger();
				final StructuredTargetExecutorLogger logger = new StructuredTargetExecutorLogger();

				final AsyncExecutor asyncExecutor = new AsyncExecutor(false);

				targetBuilderModules.execute(context, logger, asyncExecutor, result -> {

					System.out.println("### completed execution");
					
					final Log log = logger.makeLog();
					
					final LogIO logIO = new LogIO();
					
					try (FileOutputStream outputStream = new FileOutputStream(new File("buildlog.xml"))) {
						logIO.writeLog(log, outputStream);
					} catch (IOException | JAXBException ex) {
						throw new IllegalStateException(ex);
					}
				});
			}
		}
	}
}
