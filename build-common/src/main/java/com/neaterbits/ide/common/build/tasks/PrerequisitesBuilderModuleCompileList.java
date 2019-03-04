package com.neaterbits.ide.common.build.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.build.model.compile.FileCompilation;
import com.neaterbits.ide.common.build.model.compile.ModuleCompileList;
import com.neaterbits.ide.common.build.model.compile.SourceFolderCompileList;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.ide.util.scheduling.Constraint;
import com.neaterbits.ide.util.scheduling.dependencies.PrerequisitesBuildSpec;
import com.neaterbits.ide.util.scheduling.dependencies.builder.PrerequisitesBuilder;

public class PrerequisitesBuilderModuleCompileList extends PrerequisitesBuildSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Module class compile list")
			.makingProduct(ModuleCompileList.class)
			.fromItemType(SourceFolderCompileList.class)
			
			.fromIterating(Constraint.IO, (context, module) -> context.getBuildRoot().getBuildSystemRootScan().findSourceFolders(module))
	
			.buildBy(st -> st
					
				.addInfoSubTarget(
						SourceFolderResourcePath.class,
						"compilelist",
						sourceFolder -> sourceFolder.getModule().getName(),
						sourceFolder -> "Class files for source folder " + sourceFolder.getName())
				
					.withPrerequisites("Source folder compilations")
						.makingProduct(SourceFolderCompileList.class)
						.fromItemType(FileCompilation.class)
						.fromIterating(Constraint.IO, (ctx, sourceFolder) -> getSourceFiles(ctx, sourceFolder))
										
						.buildBy(sourceFileTarget -> sourceFileTarget
							.addFileSubTarget(FileCompilation.class, FileCompilation::getCompiledFile, classFile -> "Class file for source file " + classFile.getSourceFile().getName())
								.withPrerequisite("Source file")
								.from(FileCompilation::getSourcePath)
								.withFile(FileCompilation::getSourceFile)
						)
						.collectToProduct((sourceFolder, fileCompilationList) -> new SourceFolderCompileList(sourceFolder, fileCompilationList))
			)
			.collectToProduct((module, resultList) -> new ModuleCompileList(module, resultList));

	}

	private static final List<FileCompilation> getSourceFiles(ModulesBuildContext context, SourceFolderResourcePath sourceFolder) {
		
		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();

		SourceFileScanner.findSourceFiles(sourceFolder, sourceFiles);

		final TargetDirectoryResourcePath targetDirectory = context.getBuildRoot().getTargetDirectory(sourceFolder.getModule());

		return sourceFiles.stream()
				.map(sourceFile -> new FileCompilation(sourceFile, context.getLanguage().getCompiledFilePath(targetDirectory, sourceFile)))
				.collect(Collectors.toList());
	}
}
