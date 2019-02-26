package com.neaterbits.ide.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner.Namespace;
import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.ui.model.dialogs.OpenTypeDialogModel;
import com.neaterbits.ide.common.ui.model.dialogs.SuggestionType;
import com.neaterbits.ide.common.ui.model.dialogs.TypeSuggestion;
import com.neaterbits.ide.common.ui.model.text.StringTextModel;
import com.neaterbits.ide.common.ui.view.NewableSelection;
import com.neaterbits.ide.common.ui.view.UIView;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponents;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.UIComponentProvider;
import com.neaterbits.ide.util.IOUtil;
import com.neaterbits.ide.util.PathUtil;

final class UIController<WINDOW> {

	private final UIView<WINDOW> uiView;
	private final BuildRoot buildRoot;
	private final IDEComponents<WINDOW> ideComponents;
	private final ComponentIDEAccess componentIDEAccess;
	
	public UIController(UIView<WINDOW> uiView, BuildRoot buildRoot, IDEComponents<WINDOW> ideComponents) {
		
		Objects.requireNonNull(uiView);
		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(ideComponents);
		
		this.uiView = uiView;
		this.buildRoot = buildRoot;
		this.ideComponents = ideComponents;
		
		this.componentIDEAccess = new ComponentIDEAccessImpl(buildRoot, this);
	}

	private SourceFileResourcePath getCurrentEditedFile() {
		return uiView.getEditorsView().getCurrentEditedFile();
	}
	
	void refreshProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();
		
		if (!currentEditedFile.getFile().exists()) {
			closeFile(currentEditedFile);
		}
		
		uiView.getProjectView().refresh();
	}
	
	void openSourceFileForEditing(SourceFileResourcePath sourceFile) {
	
		Objects.requireNonNull(sourceFile);
		
		String text = null;
		try {
			text = IOUtil.readAll(sourceFile.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (text != null) {
			final StringTextModel textModel = new StringTextModel("\n", text);
			
			uiView.setWindowTitle(makeTitle(sourceFile));
			
			uiView.getEditorsView().displayFile(sourceFile, textModel);
		}
	}
	
	void showInProjectView(SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		uiView.getProjectView().showSourceFile(sourceFile);
	}
	
	private static String makeTitle(SourceFileResourcePath sourceFile) {
		return sourceFile.getModule().getName()
			 + '/' + PathUtil.removeDirectoryFromPath(sourceFile.getModule().getFile(), sourceFile.getFile());
	}
	
	void askOpenType() {
		
		final TypeSuggestion typeSuggestion = uiView.askOpenType(new OpenTypeDialogModel() {
				
				@Override
				public Collection<TypeSuggestion> getSuggestions(String searchText) {
					return findSuggestions(searchText, buildRoot);
				}
			});
		
		if (typeSuggestion != null) {
			openSourceFileForEditing(typeSuggestion.getSourceFile());
		}
	}
	
	void askCreateNewable() {
	
		final List<NewableCategory> newables = ideComponents.getNewableCategories();
		
		final NewableSelection newableSelection = uiView.askCreateNewable(newables);
		
		System.out.println("NewableSelection: " + newableSelection);
		
		if (newableSelection != null) {
		
			final UIComponentProvider<WINDOW> uiComponentProvider = ideComponents.findUIComponentProvider(
					newableSelection.getCategory(),
					newableSelection.getNewable());
			
			final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();
			
			uiView.openNewableDialog(
					uiComponentProvider,
					newableSelection.getCategory(),
					newableSelection.getNewable(),
					currentEditedFile != null ? currentEditedFile.getSourceFolderPath() : null,
					currentEditedFile != null ? currentEditedFile.getNamespacePath() : null,
					componentIDEAccess);
		}
		
	}
	
	void showCurrentEditedInProjectView() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			uiView.getProjectView().showSourceFile(currentEditedFile);
		}
	}
	
	void closeCurrentEditedFile() {
		
		final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

		if (currentEditedFile != null) {
			uiView.getEditorsView().closeFile(currentEditedFile);
		}
	}
	
	void minMaxEditors() {
		uiView.minMaxEditors();
	}

	void deleteResource(ResourcePath resourcePath) {
		
		Objects.requireNonNull(resourcePath);
		
		if (resourcePath instanceof SourceFileResourcePath) {
			final SourceFileResourcePath sourceFile = (SourceFileResourcePath)resourcePath;
		
			sourceFile.getFile().delete();
			
			final SourceFileResourcePath currentEditedFile = getCurrentEditedFile();

			if (sourceFile.equals(currentEditedFile)) {
				closeFile(sourceFile);
			}
		}
		
		uiView.getProjectView().refresh();
	}

	private void closeFile(SourceFileResourcePath sourceFile) {
		uiView.getEditorsView().closeFile(sourceFile);
		
		uiView.setWindowTitle("");
	}
	
	private static Collection<TypeSuggestion> findSuggestions(String searchText, BuildRoot buildRoot) {

		final List<TypeSuggestion> suggestions = new ArrayList<TypeSuggestion>();

		buildRoot.forEachSourceFolder(sourceFolder -> {
				findSuggestions(sourceFolder, searchText, suggestions);
			
				return null;
		});
		
		Collections.sort(suggestions, (t1, t2) -> t1.getName().compareTo(t2.getName()));
		
		return suggestions;
	}
	
	private static void findSuggestions(SourceFolderResourcePath sourceFolder, String searchText, List<TypeSuggestion> suggestions) {
		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();
		
		SourceFileScanner.findSourceFiles(
				sourceFolder,
				(folder, file) -> {
					
					final Namespace namespace = SourceFileScanner.getNamespaceResource(folder.getFile(), file);
					
					final NamespaceResourcePath namespaceResourcePath = new NamespaceResourcePath(sourceFolder, namespace.getNamespace());

					return new SourceFileResourcePath(namespaceResourcePath, new SourceFileResource(file));
				},
				sourceFiles);
		
		for (SourceFileResourcePath sourceFile : sourceFiles) {
			
			if (	searchText.isEmpty()
				 || sourceFile.getName().toLowerCase().startsWith(searchText.toLowerCase())) {
			
				final TypeSuggestion suggestion = new TypeSuggestion(
						SuggestionType.CLASS,
						sourceFile.getName().replaceAll(".java", ""),
						sourceFile.getFile().getPath(),
						sourceFile);
				
				suggestions.add(suggestion);
			}
		}
	}
}
