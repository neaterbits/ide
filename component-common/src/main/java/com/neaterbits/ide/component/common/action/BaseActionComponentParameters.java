package com.neaterbits.ide.component.common.action;

import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.actions.BaseActionParameters;
import com.neaterbits.ide.component.common.language.Languages;

public abstract class BaseActionComponentParameters
    extends BaseActionParameters {

    private final Languages languages;

    protected BaseActionComponentParameters(BuildRoot buildRoot, Languages languages) {
        super(buildRoot);

        Objects.requireNonNull(languages);
        
        this.languages = languages;
    }

    @Override
    public final <T> T getSourceFileLanguage(
            Class<T> languageInterface,
            SourceFileResourcePath sourceFileResourcePath) {

        return languages.getSourceFileLanguage(languageInterface, sourceFileResourcePath);
    }
}
