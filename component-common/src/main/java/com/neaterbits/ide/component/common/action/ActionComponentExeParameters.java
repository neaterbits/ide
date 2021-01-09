package com.neaterbits.ide.component.common.action;

import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.ide.common.ui.actions.ActionExeParameters;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsFinder;
import com.neaterbits.ide.component.common.language.Languages;
import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;
import com.neaterbits.ide.component.common.ui.ComponentDialogContext;
import com.neaterbits.util.threads.ForwardResultToCaller;

public abstract class ActionComponentExeParameters
        extends BaseActionComponentParameters
        implements ActionExeParameters {

    private final ForwardResultToCaller forwardResultToCaller;
    private final ComponentDialogContext dialogContext;
    private final ComponentCompositeContext compositeContext;
    private final ComponentIDEAccess componentIDEAccess;

    protected ActionComponentExeParameters(
            BuildRoot buildRoot,
            ForwardResultToCaller forwardResultToCaller,
            Languages languages,
            ComponentDialogContext dialogContext,
            ComponentCompositeContext compositeContext,
            ComponentIDEAccess componentIDEAccess) {

        super(buildRoot, languages);

        Objects.requireNonNull(forwardResultToCaller);
        Objects.requireNonNull(dialogContext);
        Objects.requireNonNull(compositeContext);
        Objects.requireNonNull(componentIDEAccess);
        
        this.forwardResultToCaller = forwardResultToCaller;
        this.dialogContext = dialogContext;
        this.compositeContext = compositeContext;
        this.componentIDEAccess = componentIDEAccess;
    }

    @Override
    public final ForwardResultToCaller getForwardResultToCaller() {
        return forwardResultToCaller;
    }

    public final ComponentDialogContext getDialogContext() {
        return dialogContext;
    }

    public final ComponentCompositeContext getCompositeContext() {
        return compositeContext;
    }

    public final ComponentIDEAccess getComponentIDEAccess() {
        return componentIDEAccess;
    }
}
