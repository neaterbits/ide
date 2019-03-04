package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.ide.common.ui.actions.types.clipboard.CopyAction;
import com.neaterbits.ide.common.ui.actions.types.clipboard.CutAction;
import com.neaterbits.ide.common.ui.actions.types.clipboard.PasteAction;
import com.neaterbits.ide.common.ui.actions.types.edit.CloseEditedAction;
import com.neaterbits.ide.common.ui.actions.types.edit.MinMaxEditorsAction;
import com.neaterbits.ide.common.ui.actions.types.entity.DeleteAction;
import com.neaterbits.ide.common.ui.actions.types.entity.NewDialogAction;
import com.neaterbits.ide.common.ui.actions.types.entity.NewPopupAction;
import com.neaterbits.ide.common.ui.actions.types.navigate.OpenTypeAction;
import com.neaterbits.ide.common.ui.actions.types.navigate.ShowInProjectsAction;
import com.neaterbits.ide.common.ui.translation.Translateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;

public enum BuiltinAction implements Translateable {

	NEW_POPUP(NewPopupAction.class),
	NEW_DIALOG(NewDialogAction.class),
	DELETE(DeleteAction.class),
	
	CUT(CutAction.class),
	PASTE(PasteAction.class),
	COPY(CopyAction.class),
	
	OPEN_TYPE(OpenTypeAction.class),
	SHOW_IN_PROJECTS(ShowInProjectsAction.class),
	
		
	CLOSE_EDITED(CloseEditedAction.class),
	MIN_MAX_EDITORS(MinMaxEditorsAction.class);
	
	private final Class<? extends Action> actionClass;
	private final Action action;
	
	private BuiltinAction(Class<? extends Action> actionClass) {
		this.actionClass = actionClass;
		try {
			this.action = actionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	public Class<? extends Action> getActionClass() {
		return actionClass;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public String getTranslationNamespace() {
		return TranslationNamespaces.BUILTIN_ACTIONS;
	}

	@Override
	public String getTranslationId() {
		return name().toLowerCase();
	}
}
