package com.neaterbits.ide.component.common.ui;

import com.neaterbits.ide.component.common.action.ActionComponentExeParameters;

public interface DialogComponentUI extends ComponentUI {

    void openDialog(
            ComponentDialogContext dialogContext,
            ActionComponentExeParameters parameters);

}
