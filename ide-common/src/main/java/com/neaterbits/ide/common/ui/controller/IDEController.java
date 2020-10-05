package com.neaterbits.ide.common.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.resource.NamespaceResource;
import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.model.source.SourceFilesModel;
import com.neaterbits.ide.common.ui.UI;
import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.keys.IDEKeyBindings;
import com.neaterbits.ide.common.ui.keys.Key;
import com.neaterbits.ide.common.ui.keys.KeyBindings;
import com.neaterbits.ide.common.ui.keys.KeyCombination;
import com.neaterbits.ide.common.ui.keys.KeyLocation;
import com.neaterbits.ide.common.ui.keys.KeyMask;
import com.neaterbits.ide.common.ui.menus.IDEMenus;
import com.neaterbits.ide.common.ui.menus.MenuItemEntry;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.MenuSelectionListener;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.common.ui.view.ViewMenuItem;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;
import com.neaterbits.ide.util.Value;

public final class IDEController implements ComponentIDEAccess {

	private final BuildRoot buildRoot;
	private final EditUIController uiController;
	
	private final ActionExecuteState actionExecuteState;
	private final ActionApplicableParameters actionApplicableParameters;
	
	private final Map<MenuItemEntry, ViewMenuItem> menuMap;
	
	private final UIViewAndSubViews uiView;
	
	private View focusedView;
	
	public IDEController(
			BuildRoot buildRoot,
			UI ui,
			TextEditorConfig config,
			IDEComponents ideComponents,
			SourceFilesModel sourceFilesModel,
			CodeMapModel codeMapModel) {

		Objects.requireNonNull(buildRoot);
		
		this.buildRoot = buildRoot;
		
		final ProjectsModel projectModel = new ProjectsModel(buildRoot);

		final Translator uiTranslator = new IDETranslator();
		final KeyBindings keyBindings = IDEKeyBindings.makeKeyBindings();

		final Menus menus = IDEMenus.makeMenues(keyBindings);
		
		final UIModels uiModels = new UIModels(projectModel);
		
		final UIParameters uiParameters = new UIParameters(uiTranslator, keyBindings, uiModels, config);
		
		this.menuMap = new HashMap<>();
		
		final MenuSelectionListener menuListener = menuItemEntry -> {
			callMenuItemAction(menuItemEntry);
		};
		
		this.uiView = ui.makeUIView(uiParameters, menus, (menuItemEntry, viewMenuItem) -> {
			menuMap.put(menuItemEntry, viewMenuItem);
			
			return menuListener;
		});
		
		this.uiController = new EditUIController(uiView, config, projectModel, ideComponents, sourceFilesModel, codeMapModel);
		
		final Clipboard clipboard = new ClipboardImpl(ui.getSystemClipboard());
		
		this.actionExecuteState = new ActionExecuteState(
				ideComponents,
				uiView,
				clipboard,
				new UndoRedoBuffer() {
					
					@Override
					public boolean hasUndoEntries() {
						return true;
					}
					
					@Override
					public boolean hasRedoEntries() {
						return false;
					}
				},
				this,
				buildRoot,
				uiController,
				codeMapModel);

		this.actionApplicableParameters = new ActionApplicableParametersImpl(actionExecuteState);
		
		uiView.addKeyEventListener(new KeyEventListener() {
			
			@Override
			public boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location) {
				
				return true;
			}
			
			@Override
			public boolean onKeyPress(Key key, KeyMask mask, KeyLocation location) {
				
				final Action action = findActionWithNoKeyBindingInMenus(keyBindings, menus, new KeyCombination(key, mask));

				if (action != null) {
					
					if (action.isApplicableInContexts(
							actionApplicableParameters,
							getFocusedViewActionContexts(),
							getAllActionContexts(uiView))) {
					
					
						action.execute(makeActionExecuteParameters());
					}
				}
				
				return true;
			}
		});

		uiView.getViewList().addActionContextViewListener((view, updatedContexts) -> {
			updateMenuItemsEnabledState(uiView, actionApplicableParameters);
		});

		// initial update
		updateMenuItemsEnabledState(uiView, actionApplicableParameters);
		
		ui.addFocusListener(view -> {
			focusedView = view;
			
			updateMenuItemsEnabledState(uiView, actionApplicableParameters);
		});
	
	}

	public UIViewAndSubViews getMainView() {
        return uiView;
    }

    private static Action findActionWithNoKeyBindingInMenus(KeyBindings keyBindings, Menus menus, KeyCombination keyCombination) {

		Objects.requireNonNull(keyBindings);
		Objects.requireNonNull(menus);
		Objects.requireNonNull(keyCombination);
		
		final Value<Boolean> found = new Value<>();
		
		menus.iterateItems(menuItem -> {
			if (keyCombination.equals(menuItem.getKeyCombination())) {
				found.set(true);
			}
		});
		
		return found.get() != null && found.get()
				? null
				: keyBindings.findAction(keyCombination.getKey(), keyCombination.getQualifiers());
	}
	
	private ActionExecuteParameters makeActionExecuteParameters() {
		
		final ActionExecuteParameters parameters = new ActionExecuteParametersImpl(
				actionExecuteState,
				focusedView,
				uiController.getCurrentEditor(),
				uiController.getCurrentEditedFile());
	
		return parameters;
	}
	
	private void callMenuItemAction(MenuItemEntry menuItem) {
		
		Objects.requireNonNull(menuItem);
		
		menuItem.execute(makeActionExecuteParameters());
	}
	
	private void updateMenuItemsEnabledState(UIViewAndSubViews uiView, ActionApplicableParameters applicableParameters) {

		final ActionContexts focusedViewActionContexts = getFocusedViewActionContexts();
		final ActionContexts allActionContexts = getAllActionContexts(uiView);
		
		// Change menu enabled state depending on applicable contexts
		for (MenuItemEntry entry : menuMap.keySet()) {
			final boolean applicable = entry.isApplicableInContexts(
					applicableParameters,
					focusedViewActionContexts,
					allActionContexts);
		
			final ViewMenuItem viewMenuItem = menuMap.get(entry);
		
			viewMenuItem.setEnabled(applicable);
		}
	}
	
	private ActionContexts getFocusedViewActionContexts() {
		return new ActionContextsImpl(focusedView != null ? focusedView.getActiveActionContexts() : null);
	}

	private static ActionContexts getAllActionContexts(UIViewAndSubViews uiViews) {
		
		final List<ActionContext> actionContexts = new ArrayList<>();
		
		for (View view : uiViews.getViewList().getViews()) {
			
			final Collection<ActionContext> viewActionContexts = view.getActiveActionContexts();
			
			if (viewActionContexts != null) {
				actionContexts.addAll(viewActionContexts);
			}
		}
		
		return new ActionContextsImpl(actionContexts);
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
