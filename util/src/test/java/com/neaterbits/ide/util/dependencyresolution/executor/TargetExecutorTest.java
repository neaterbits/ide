package com.neaterbits.ide.util.dependencyresolution.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.ide.util.Value;
import com.neaterbits.ide.util.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.ide.util.dependencyresolution.model.FileTarget;
import com.neaterbits.ide.util.dependencyresolution.model.InfoTarget;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisite;
import com.neaterbits.ide.util.dependencyresolution.model.Prerequisites;
import com.neaterbits.ide.util.dependencyresolution.model.Target;
import com.neaterbits.ide.util.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.ide.util.scheduling.QueueAsyncExecutor;
import com.neaterbits.ide.util.scheduling.task.TaskContext;
import com.neaterbits.structuredlog.binary.logging.LogContext;

public class TargetExecutorTest {

	@Test
	public void testTarget() throws IOException {
		
		final File targetObject = new File("/");
		
		// final File file = File.createTempFile("test", "file");
		
		// file.deleteOnExit();
		
		final RecursiveBuildInfo<TaskContext, File, File> buildInfo = new RecursiveBuildInfo<>(
				(context, target) -> {
					
					System.out.println("## target " + target);
					
					final Collection<File> result;

					int level = 0;
					
					for (File parentFile = target; parentFile != null; parentFile = parentFile.getParentFile()) {
						++ level;
					}

					if (level > 1) {
						result = Collections.emptyList();
					}
					else {
					
						final File [] files = target.listFiles();
						
						result = files != null ? Arrays.asList(files) : Collections.emptyList();
					}
					
					return result;
				},
				Function.identity());
		
		final Value<ArrayList<File>> collectedFiles = new Value<>();
		
		final CollectSubTargets<String> collectSubTargets = new CollectSubTargets<>(ArrayList.class, (infoTarget, files) -> {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final ArrayList<File> arrayList = new ArrayList(files);
			
			if (collectedFiles.get() != null) {
				throw new IllegalStateException();
			}
			
			collectedFiles.set(arrayList);
			
			return arrayList;
		});
		
		final Collectors<String> collectors = new Collectors<>(collectSubTargets, null);
		
		final LogContext logContext = new LogContext();
		
		final Prerequisites prerequisites = new Prerequisites(
				logContext, 
				Arrays.asList(
						new Prerequisite<File>(
								logContext, 
								targetObject,
								new FileTarget<File>(
										logContext, 
										File.class,
										targetObject,
										(context, f) -> f,
										f -> (File)f,
										f -> "File target " + f,
										targetObject,
										Collections.emptyList(),
										new Action<File>(null, (context, target, params) -> {
											return new ActionLog("1234", 0);
										}),
										null))),
				"Info prerequisites",
				buildInfo,
				collectors);
		
		final String infoTargetObj = "the target object";
		
		final Target<String> infoTarget = new InfoTarget<>(
				logContext, 
				String.class,
				"test info target",
				obj -> "Qualifier name",
				obj -> "Target object " + obj,
				infoTargetObj,
				Arrays.asList(prerequisites),
				new Action<>(null, (context, target, params) -> {
					
					try {
						final ArrayList<?> files = params.getCollectedProduct(infoTargetObj, ArrayList.class);
						
						if (files == null) {
							throw new AssertionError();
						}
						
						if (files.size() != targetObject.listFiles().length) {
							throw new AssertionError();
						}
					}
					catch (Exception ex) {
						throw new AssertionError();
					}
					
					
					return new ActionLog("1234", 0);
				}),
				null);
		
		
		final TargetExecutor targetExecutor = new TargetExecutor(new QueueAsyncExecutor(false));
	
		final TaskContext taskContext = new TaskContext();
		
		targetExecutor.runTargets(
				taskContext,
				infoTarget,
				new PrintlnTargetExecutorLogger(),
				builResult -> {

					
			assertThat(collectedFiles.get()).isNotNull();
			
			final File [] files = targetObject.listFiles();
			
			assertThat(collectedFiles.get().size()).isEqualTo(files.length);
			
			for (File file : files) {
				assertThat(collectedFiles.get().contains(file)).isTrue();
			}
					
			System.out.println("Build finished");
		});
	}
	
}