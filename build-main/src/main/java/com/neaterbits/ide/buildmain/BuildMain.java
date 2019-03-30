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
import com.neaterbits.ide.component.java.language.JavaBuildableLanguage;
import com.neaterbits.ide.component.java.language.JavaCompiler;
import com.neaterbits.ide.util.scheduling.QueueAsyncExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.BinaryTargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.dependencies.DelegatingTargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.dependencies.StructuredTargetExecutorLogger;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.structuredlog.xml.model.Log;
import com.neaterbits.structuredlog.xml.model.LogIO;

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
						new JavaBuildableLanguage(),
						new JavaCompiler(),
						null);

				// final TargetExecutorLogger logger = new PrintlnTargetExecutorLogger();
				final StructuredTargetExecutorLogger structuredLogger = new StructuredTargetExecutorLogger();

				final LogContext logContext = new LogContext();
				
				final BinaryTargetExecutorLogger binaryTargetExecutorLogger = new BinaryTargetExecutorLogger(logContext);
				
				final DelegatingTargetExecutorLogger delegatingLogger = new DelegatingTargetExecutorLogger(
						structuredLogger,
						binaryTargetExecutorLogger);
				
				final QueueAsyncExecutor asyncExecutor = new QueueAsyncExecutor(false);

				targetBuilderModules.execute(logContext, context, delegatingLogger, asyncExecutor, result -> {

					System.out.println("### completed execution");
					
					final Log log = structuredLogger.makeLog();
					
					final LogIO logIO = new LogIO();
					
					try (FileOutputStream outputStream = new FileOutputStream(new File("buildlog.xml"))) {
						logIO.writeLog(log, outputStream);
					} catch (IOException | JAXBException ex) {
						throw new IllegalStateException(ex);
					}

					try (FileOutputStream outputStream = new FileOutputStream(new File("binarylog"))) {
						logContext.writeLogBufferToOutput(outputStream);
					} catch (IOException ex) {
						throw new IllegalStateException(ex);
					}
				});
			}
		}
	}
}
