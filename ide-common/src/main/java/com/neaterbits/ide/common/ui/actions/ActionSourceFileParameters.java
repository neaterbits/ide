package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.source.SourceFileModel;

public interface ActionSourceFileParameters {

    SourceFileResourcePath getCurrentSourceFileResourcePath();

    SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath);
    
    <T> T getSourceFileLanguage(Class<T> languageInterface, SourceFileResourcePath sourceFile);

    BuildRoot getBuildRoot();
}
