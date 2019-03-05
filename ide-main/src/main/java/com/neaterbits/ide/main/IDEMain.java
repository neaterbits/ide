package com.neaterbits.ide.main;

import java.io.File;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.model.codemap.CodeMapGatherer;
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
import com.neaterbits.ide.util.scheduling.dependencies.PrintlnTargetExecutorLogger;

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
				final SWTUI ui = new SWTUI();
				
				final BuildSystems buildSystems = new BuildSystems(); 

				final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);
				
				final BuildRoot buildRoot = new BuildRootImpl<>(projectDir, buildSystem.scan(projectDir));  
				
				final IDEComponents ideComponents = registerComponents();
				
				final TextEditorConfig config = new TextEditorConfig(4, true);
				
				final CodeMapGatherer codeMapGatherer = new CodeMapGatherer(new JavaBytecodeFormat());
				
				new IDEController(buildRoot, ui, config, ideComponents);
				
				startIDEScanJobs(buildRoot, codeMapGatherer);
				
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
	

	private static void startIDEScanJobs(BuildRoot buildRoot, CodeMapGatherer codeMapGatherer) {
	
		final TargetBuilderIDEStartup ideStartup = new TargetBuilderIDEStartup();
		final InitialScanContext context = new InitialScanContext(buildRoot, new JavaLanguage(), codeMapGatherer);
		
		ideStartup.execute(context, new PrintlnTargetExecutorLogger(), null);
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
