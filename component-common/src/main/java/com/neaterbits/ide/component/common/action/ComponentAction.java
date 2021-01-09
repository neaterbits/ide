package com.neaterbits.ide.component.common.action;

import com.neaterbits.ide.common.ui.actions.TranslatedAction;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.common.IDEComponent;

public abstract class ComponentAction
    extends TranslatedAction<ActionComponentAppParameters, ActionComponentExeParameters> {

    protected ComponentAction(Class<? extends IDEComponent> componentType, String translationId) {
        super(Translator.getComponentNamespace(componentType), translationId);
    
        System.out.println("## component action " + translationId);
    }
}
