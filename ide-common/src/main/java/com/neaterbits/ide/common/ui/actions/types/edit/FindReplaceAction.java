package com.neaterbits.ide.common.ui.actions.types.edit;


import com.neaterbits.ide.common.ui.actions.Action;
import com.neaterbits.ide.common.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.common.ui.actions.contexts.EditorContext;
import com.neaterbits.ide.common.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.common.ui.view.dialogs.FindReplaceDialog;
import com.neaterbits.ide.util.Value;
import com.neaterbits.ide.util.ui.text.StringText;

public final class FindReplaceAction extends Action {

	private FindReplaceDialogModel curDialogModel;
	
	public FindReplaceAction() {
		this.curDialogModel = FindReplaceDialogModel.INITIAL;
	}

	@Override
	public void execute(ActionExecuteParameters parameters) {

		final Value<Long> curFound = new Value<>();
		
		parameters.getUIDialogs().askFindReplace(curDialogModel, dialog -> {

			dialog.addFindTextChangeListener(text -> updateModelAndButtonState(dialog, curFound));
			dialog.addFindTextEnterKeyListener(() -> {
				
				updateModelAndButtonState(dialog, curFound);
				
				if (hasSearchText(curDialogModel)) {
					findText(parameters, dialog, curFound);
				}
			});

			dialog.addReplaceWithTextListener(text -> updateModel(dialog));
			
			dialog.addDirectionForwardListener(() -> updateModel(dialog));
			dialog.addDirectionBackwardListener(() -> updateModel(dialog));
			dialog.addScopeAllListener(() -> updateModel(dialog));
			dialog.addScopeSelectedLinesListener(() -> updateModel(dialog));
			dialog.addOptionsCaseSensitiveListener(() -> updateModel(dialog));
			dialog.addOptionsWrapSearchListener(() -> updateModel(dialog));
			dialog.addOptionsWholeWordListener(() -> updateModel(dialog));

			updateDialogButtonState(curDialogModel, dialog, curFound);
			
			if (!curDialogModel.getSearchFor().isEmpty()) {
				dialog.setFindTextSelected();
			}
			
			dialog.addFindButtonListener(() -> {
				findText(parameters, dialog, curFound);
			});
			
			dialog.addReplaceFindButtonListener(() -> {
				
				replaceText(curFound.get(), parameters);
				
				findText(parameters, dialog, curFound);
			});
			
			dialog.addReplaceButtonListener(() -> {
				replaceText(curFound.get(), parameters);
				
				curFound.set(null);
				
				updateDialogButtonState(curDialogModel, dialog, curFound);
			});

			dialog.addReplaceAllButtonListener(() -> {
				
				long foundPos;
				
				while (-1 != (foundPos = findText(parameters, dialog, curFound))) {
					replaceText(foundPos, parameters);
				}
			});
		});
	}
	
	private long findText(ActionExecuteParameters parameters, FindReplaceDialog dialog, Value<Long> curFound) {
		
		final long foundPos = parameters.getFocusedEditor().find(
				curFound.get() != null
					? curFound.get() + 1
					: -1L,
				new StringText(curDialogModel.getSearchFor()),
				curDialogModel.getDirection(),
				curDialogModel.getScope(),
				curDialogModel.isCaseSensitive(),
				curDialogModel.isWrap(),
				curDialogModel.isWholeWord());
		
		curFound.set(foundPos != -1 ? foundPos : null);

		updateDialogButtonState(curDialogModel, dialog, curFound);
		
		return foundPos;
	}
	
	private void replaceText(long pos, ActionExecuteParameters parameters) {
		
		parameters.getFocusedEditor().replace(
				pos,
				curDialogModel.getSearchFor().length(),
				new StringText(curDialogModel.getReplaceWith()));
		
	}

	private void updateModel(FindReplaceDialog dialog) {

		this.curDialogModel = dialog.getModel();
	}

	private void updateModelAndButtonState(FindReplaceDialog dialog, Value<Long> curFound) {
		
		updateModel(dialog);
		
		updateDialogButtonState(curDialogModel, dialog, curFound);
	}
	
	private void updateDialogButtonState(FindReplaceDialogModel model, FindReplaceDialog dialog, Value<Long> curFound) {
		
		final boolean hasSearchText = hasSearchText(model);
		
		dialog.setFindButtonEnabled(hasSearchText);
		
		dialog.setReplaceFindButtonEnabled(curFound.get() != null && hasSearchText);
		dialog.setReplaceButtonEnabled(curFound.get() != null && hasSearchText);
		
		dialog.setReplaceAllButtonEnabled(hasSearchText);
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {

		return focusedContexts.hasOfType(EditorContext.class);
	}

	private static boolean hasSearchText(FindReplaceDialogModel model) {
		return !model.getSearchFor().isEmpty();
	}
}
