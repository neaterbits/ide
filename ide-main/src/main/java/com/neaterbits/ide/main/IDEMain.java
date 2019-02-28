package com.neaterbits.ide.main;

import java.io.File;

import org.eclipse.swt.widgets.Shell;

import com.neaterbits.ide.common.buildsystem.BuildSystem;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.model.BuildRootImpl;
import com.neaterbits.ide.common.build.tasks.InitialScanContext;
import com.neaterbits.ide.common.build.tasks.TargetBuilderInitialScan;
import com.neaterbits.ide.common.ui.controller.IDEController;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.component.java.language.JavaCompileableLanguage;
import com.neaterbits.ide.component.java.language.JavaLanguageComponent;
import com.neaterbits.ide.component.java.ui.JavaUIComponentProvider;
import com.neaterbits.ide.swt.SWTUI;
import com.neaterbits.ide.util.scheduling.dependencies.PrintlnTargetExecutorLogger;
import com.neaterbits.ide.util.scheduling.dependencies.TargetExecutor;
import com.neaterbits.ide.util.scheduling.dependencies.TargetFinder;
import com.neaterbits.ide.util.scheduling.dependencies.TargetFinderLogger;
import com.neaterbits.ide.util.scheduling.dependencies.TargetSpec;

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
				
				final IDEComponents<Shell> ideComponents = registerComponents();
				
				final TextEditorConfig config = new TextEditorConfig(4, true);
				
				final IDEController<Shell> ideController = new IDEController<>(buildRoot, ui, config, ideComponents);
				
				startIDEScanJobs(buildRoot);
				
				ui.main();
			}
		}
		}
		catch (Exception ex) {
			System.err.println("Exception at " + ex.getMessage());

			printStackTrace(ex.getStackTrace(), 5);
		}
	}
	
	private static IDEComponents<Shell> registerComponents() {

		final IDEComponents<Shell> components = new IDEComponents<>();
		
		components.registerComponent(new JavaLanguageComponent(), new JavaUIComponentProvider());
		
		return components;
	}
	

	private static void startIDEScanJobs(BuildRoot buildRoot) {
		
		final TargetSpec<InitialScanContext, ?, ?> targetSpec = TargetBuilderInitialScan.makeTargetBuilder();
		final TargetFinder targetFinder = new TargetFinder();
		final InitialScanContext context = new InitialScanContext(buildRoot, new JavaCompileableLanguage());
		
		final TargetFinderLogger targetFinderLogger = null; // new PrintlnTargetFinderLogger();
		
		targetFinder.computeTargets(targetSpec, context, targetFinderLogger, target -> {
			
			target.printTargets();
			
			final TargetExecutor targetExecutor = new TargetExecutor();
			
			targetExecutor.runTargets(context, target, new PrintlnTargetExecutorLogger());
		});
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
