package com.neaterbits.ide.common.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.resource.NamespaceResource;
import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.SourceFileHolderResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.ui.UI;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;

public final class IDEController<WINDOW> implements ComponentIDEAccess {

	private final BuildRoot buildRoot;
	private final EditUIController<WINDOW> uiController;
	
	public IDEController(BuildRoot buildRoot, UI<WINDOW> ui, TextEditorConfig config, IDEComponents<WINDOW> ideComponents) {

		Objects.requireNonNull(buildRoot);
		
		this.buildRoot = buildRoot;
		
		final ProjectsModel projectModel = new ProjectsModel(buildRoot);

		final UIViewAndSubViews<WINDOW> uiView = ui.makeUIView(projectModel, config);

		this.uiController = new EditUIController<>(uiView, buildRoot, projectModel, this, ideComponents);
		
		final KeyEventListener keyEventListener = new IDEKeyListener(uiController);

		uiView.addKeyEventListener(keyEventListener);
	}

	@Override
	public void writeAndOpenFile(
			String projectName,
			String sourceFolder,
			String [] namespacePath,
			String fileName,
			String text) throws IOException {

		final SourceFolderResourcePath folder = findSourceFolder(projectName, sourceFolder);
		
		final SourceFileHolderResourcePath sourceFileHolderPath = namespacePath != null
				? new NamespaceResourcePath(
						folder,
						new NamespaceResource(
								new File(folder.getFile(), Strings.join(namespacePath, File.separatorChar)),
								namespacePath))
				: folder;
		
		final File sourceDirectory = sourceFileHolderPath.getFile();
		
		if (!sourceDirectory.exists()) {
			if (!sourceDirectory.mkdirs()) {
				throw new IOException("Failed to create directory " + sourceDirectory.getPath());
			}
		}
		else {
			if (!sourceDirectory.isDirectory()) {
				throw new IOException("Source directory exists but is not a directory");
			}
		}
						
		final File file = new File(sourceDirectory, fileName);
						
		if (file.exists()) {
			throw new IOException("Source file already exists: " + file.getPath());
		}
		
		IOUtil.writeAll(file, text);
		
		final SourceFileResourcePath sourceFile = new SourceFileResourcePath(
				sourceFileHolderPath,
				new SourceFileResource(file));
		
		uiController.openSourceFileForEditing(sourceFile);
		
		uiController.showInProjectView(sourceFile, false);
	}

	
	@Override
	public File getRootPath() {
		return buildRoot.getPath();
	}

	@Override
	public boolean isValidSourceFolder(String projectName, String sourceFolder) {

		final SourceFolderResourcePath folder = findSourceFolder(projectName, sourceFolder);
		
		return folder != null;
	}
	
	private SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder) {
		return buildRoot.forEachSourceFolder(folder -> {
			
			final String sourceFolderName = PathUtil.removeDirectoryFromPath(folder.getModule().getFile(), folder.getFile());
			
			return folder.getModule().getName().equals(projectName) && sourceFolderName.equals(sourceFolder)
					? folder
					: null;
		});
	}
	
}
