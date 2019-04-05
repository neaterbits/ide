package com.neaterbits.ide.common.build.model.compile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.dependencyresolution.CollectedObject;

public final class SourceFolderCompileList implements CollectedObject {

	private final SourceFolderResourcePath sourceFolder;
	private final List<FileCompilation> fileCompilations;

	public SourceFolderCompileList(SourceFolderResourcePath sourceFolder, List<FileCompilation> fileCompilations) {
		
		Objects.requireNonNull(sourceFolder);
		Objects.requireNonNull(fileCompilations);
		
		this.sourceFolder = sourceFolder;
		this.fileCompilations = Collections.unmodifiableList(fileCompilations);
	}

	public SourceFolderResourcePath getSourceFolder() {
		return sourceFolder;
	}

	public List<FileCompilation> getFileCompilations() {
		return fileCompilations;
	}

	@Override
	public String toString() {
		return sourceFolder.getFile().getPath() + "/" + fileCompilations;
	}

	@Override
	public String getName() {
		return sourceFolder.getName();
	}

	@Override
	public List<String> getCollected() {
		return fileCompilations.stream()
				.map(fileCompilation -> fileCompilation.getSourceFile().getName())
				.collect(Collectors.toList());
	}
}
