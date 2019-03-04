package com.neaterbits.ide.common.ui.actions.types.entity;

import java.util.List;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.view.NewableSelection;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.UIComponentProvider;

public final class NewDialogAction extends NewAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
		final List<NewableCategory> newables = parameters.getComponents().getNewableCategories();
		
		final NewableSelection newableSelection = parameters.getUIDialogs().askCreateNewable(newables);
		
		System.out.println("NewableSelection: " + newableSelection);
		
		if (newableSelection != null) {
		
			final UIComponentProvider uiComponentProvider = parameters.getComponents().findUIComponentProvider(
					newableSelection.getCategory(),
					newableSelection.getNewable());
			
			final SourceFileResourcePath currentEditedFile = parameters.getCurrentEditedFile();
			
			parameters.getUIDialogs().openNewableDialog(
					uiComponentProvider,
					newableSelection.getCategory(),
					newableSelection.getNewable(),
					currentEditedFile != null ? currentEditedFile.getSourceFolderPath() : null,
					currentEditedFile != null ? currentEditedFile.getNamespacePath() : null,
					parameters.getComponentIDEAccess());
		}
	}

	@Override
	public boolean isApplicableInContexts(ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}
}
