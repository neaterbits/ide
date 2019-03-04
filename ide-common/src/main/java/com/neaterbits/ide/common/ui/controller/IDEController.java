package com.neaterbits.ide.common.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.keys.IDEKeyBindings;
import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.keys.KeyMask;
import com.neaterbits.ide.common.ui.menus.IDEMenus;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;

public final class IDEController implements ComponentIDEAccess {

	private final BuildRoot buildRoot;
	private final EditUIController uiController;
	
	private View focusedView;
	
	public IDEController(BuildRoot buildRoot, UI ui, TextEditorConfig config, IDEComponents ideComponents) {

		Objects.requireNonNull(buildRoot);
		
		this.buildRoot = buildRoot;
		
		final ProjectsModel projectModel = new ProjectsModel(buildRoot);

		final Translator uiTranslator = new IDETranslator();
		final Menus menus = IDEMenus.makeMenues();
		final KeyBindings keyBindings = IDEKeyBindings.makeKeyBindings();
		final UIParameters uiParameters = new UIParameters(uiTranslator, keyBindings, menus, config);
		
		final UIViewAndSubViews uiView = ui.makeUIView(uiParameters, projectModel);
		
		this.uiController = new EditUIController(uiView, projectModel, ideComponents);
		
		final ActionExecuteState actionExecuteState = new ActionExecuteState(
				ideComponents,
				uiView,
				this,
				buildRoot,
				uiController);

		uiView.addKeyEventListener(new KeyEventListener() {
			
			@Override
			public void onKeyRelease(Key key, KeyMask mask) {
				
			}
			
			@Override
			public void onKeyPress(Key key, KeyMask mask) {
				final Action action = keyBindings.findAction(key, mask);
				
				
				if (action != null) {

					final ActionContexts actionContexts = getActionContexts();
					
					if (action.isApplicableInContexts(actionContexts)) {
					
						final ActionExecuteParameters parameters = new ActionExecuteParametersImpl(actionExecuteState, uiController.getCurrentEditedFile());
					
						action.execute(parameters);
					}
				}
			}
		});

		uiView.getViewList().addActionContextViewListener((view, updatedContexts) -> {
			
			// Change menu enabled state
			
		});
		
		ui.addFocusListener(view -> {
			focusedView = view;
		});
	}
	
	private ActionContexts getActionContexts() {
		
		final ActionContexts actionContexts;
		
		if (focusedView == null) {
			actionContexts = new ActionContexts() {
				@Override
				public <T extends ActionContext> T getOfType(Class<T> cl) {
					
					Objects.requireNonNull(cl);
					
					return null;
				}
			};
		}
		else {
			
			final Map<Class<?>, ActionContext> map = new HashMap<>();

			final Collection<ActionContext> activeActionContexts = focusedView.getActiveActionContexts();
			
			if (activeActionContexts != null && !activeActionContexts.isEmpty()) {
				for (ActionContext actionContext : activeActionContexts) {
					map.put(actionContext.getClass(), actionContext);
				}
			}

			actionContexts = new ActionContexts() {
				@SuppressWarnings("unchecked")
				@Override
				public <T extends ActionContext> T getOfType(Class<T> cl) {
					
					Objects.requireNonNull(cl);
					
					return (T)map.get(cl);
				}
			};
		}
		
		return actionContexts;
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
