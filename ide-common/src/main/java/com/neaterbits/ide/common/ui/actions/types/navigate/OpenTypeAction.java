package com.neaterbits.ide.common.ui.actions.types.navigate;

import com.neaterbits.ide.common.model.codemap.TypeSuggestion;
import com.neaterbits.ide.common.model.codemap.TypeSuggestions;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.model.dialogs.OpenTypeDialogModel;

public class OpenTypeAction extends NavigateAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		final TypeSuggestion typeSuggestion = parameters.getUIDialogs().askOpenType(new OpenTypeDialogModel() {
				
				@Override
				public TypeSuggestions getSuggestions(String searchText) {
					return parameters.getCodeMap().findSuggestions(searchText, true);
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

}
