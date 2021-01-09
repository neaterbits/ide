package com.neaterbits.ide.component.common.action;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.ide.common.ui.actions.ActionAppParameters;
import com.neaterbits.ide.component.common.language.Languages;

public abstract class ActionComponentAppParameters
    extends BaseActionComponentAppParameters
    implements ActionAppParameters {

    protected ActionComponentAppParameters(BuildRoot buildRoot, Languages languages) {
        super(buildRoot, languages);
    }
}
