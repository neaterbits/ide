package com.neaterbits.ide.main;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.language.CompileableLanguage;
import com.neaterbits.ide.common.model.codemap.CodeMapGatherer;
import com.neaterbits.ide.common.model.source.SourceFilesModel;
import com.neaterbits.ide.common.scheduling.IDEScheduler;
import com.neaterbits.ide.common.scheduling.IDESchedulerImpl;
import com.neaterbits.ide.common.tasks.InitialScanContext;
import com.neaterbits.ide.common.tasks.TargetBuilderIDEStartup;
import com.neaterbits.compiler.java.bytecode.JavaBytecodeFormat;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.controller.IDEController;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.component.java.language.JavaLanguage;
import com.neaterbits.ide.component.java.language.JavaLanguageComponent;
import com.neaterbits.ide.component.java.ui.JavaUIComponentProvider;
import com.neaterbits.ide.swt.SWTUI;
import com.neaterbits.ide.util.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.AsyncExecutor;
import com.neaterbits.ide.util.swt.SWTAsyncExecutor;
import com.neaterbits.structuredlog.binary.logging.LogContext;

public class IDEMain {

	private static void usage() {
		System.err.println("usage: <projectdir>");
	}
	
	public static void main(String [] args) throws ScanException {
		
		try {
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
				
				final Display display = Display.getDefault();
				final AsyncExecutor asyncExecutor = new SWTAsyncExecutor(display);
				
				final SWTUI ui = new SWTUI(display);
				
				final BuildSystems buildSystems = new BuildSystems(); 

				final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);
				
				final BuildRoot buildRoot = new BuildRootImpl<>(projectDir, buildSystem.scan(projectDir));  
				
				final IDEComponents ideComponents = registerComponents();
				
				final TextEditorConfig config = new TextEditorConfig(4, true);
				
				final CompileableLanguage language = new JavaLanguage();

				final CodeMapGatherer codeMapGatherer = new CodeMapGatherer(asyncExecutor, language, new JavaBytecodeFormat(), buildRoot);

				final IDEScheduler ideScheduler = new IDESchedulerImpl(asyncExecutor);
				
				final SourceFilesModel sourceFilesModel = new SourceFilesModel(ideScheduler, ideComponents.getLanguages(), codeMapGatherer);
				
				new IDEController(buildRoot, ui, config, ideComponents, sourceFilesModel, codeMapGatherer.getModel());
				
				// Run events on event queue before async jobs send event on event queue
				ui.runInitialEvents();
				
				final LogContext logContext = new LogContext();
				
				startIDEScanJobs(logContext, asyncExecutor, buildRoot, language, codeMapGatherer);
				
				ui.main();
			}
		}
		}
		catch (Exception ex) {
			System.err.println("Exception at " + ex.getMessage());

			printStackTrace(ex.getStackTrace(), 5);
		}
	}
	
	private static IDEComponents registerComponents() {

		final IDEComponents components = new IDEComponents();
		
		components.registerComponent(new JavaLanguageComponent(), new JavaUIComponentProvider());
		
		return components;
	}
	

	private static void startIDEScanJobs(
			LogContext logContext,
			AsyncExecutor asyncExecutor,
			BuildRoot buildRoot, 
			CompileableLanguage language,
			CodeMapGatherer codeMapGatherer) {
	
		final TargetBuilderIDEStartup ideStartup = new TargetBuilderIDEStartup();
		final InitialScanContext context = new InitialScanContext(buildRoot, language, codeMapGatherer);
		
		ideStartup.execute(logContext, context, new PrintlnTargetExecutorLogger(), asyncExecutor, null);
	}
	
	private static void printStackTrace(StackTraceElement [] stackTrace, int num) {
		
		for (int i = 0; i < num; ++ i) {
			final StackTraceElement stackTraceElement = stackTrace[i];
			
			System.err.println("    "
			+ stackTraceElement.getClassName() + "."
			+ stackTraceElement.getMethodName() 
			+ ":" + stackTraceElement.getLineNumber());
		}
	}
}
