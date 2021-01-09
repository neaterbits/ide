package com.neaterbits.ide.component.runners.ui;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.component.common.IDEComponent;
import com.neaterbits.ide.component.common.action.ActionComponentAppParameters;
import com.neaterbits.ide.component.common.action.ActionComponentExeParameters;
import com.neaterbits.ide.component.common.action.ComponentAction;

final class RunConfigurationsAction extends ComponentAction {

    public RunConfigurationsAction(
            Class<? extends IDEComponent> componentType,
            String translationId) {

        super(componentType, translationId);
    }

    @Override
    public void execute(ActionComponentExeParameters parameters) {

        final RunnersComponentUI runnersComponentUI = new RunnersComponentUI();
        
        runnersComponentUI.openDialog(parameters.getDialogContext(), parameters);
    }

    @Override
    public boolean isApplicableInContexts(
            ActionComponentAppParameters parameters,
            ActionContexts focusedContexts,
            ActionContexts allContexts) {

        return true;
    }
}
