package com.neaterbits.ide.common.ui.actions.types.navigate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner;
import com.neaterbits.ide.common.build.tasks.util.SourceFileScanner.Namespace;
import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResource;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.model.dialogs.OpenTypeDialogModel;
import com.neaterbits.ide.common.ui.model.dialogs.SuggestionType;
import com.neaterbits.ide.common.ui.model.dialogs.TypeSuggestion;

public class OpenTypeAction extends NavigateAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		final TypeSuggestion typeSuggestion = parameters.getUIDialogs().askOpenType(new OpenTypeDialogModel() {
				
				@Override
				public Collection<TypeSuggestion> getSuggestions(String searchText) {
					return findSuggestions(searchText, parameters.getBuildRoot());
				}
			});
		
		if (typeSuggestion != null) {
			parameters.getEditActions().openSourceFileForEditing(typeSuggestion.getSourceFile());
		}
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
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
