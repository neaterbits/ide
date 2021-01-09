package com.neaterbits.ide.component.common.runner;


import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public interface RunnableLanguage {

    boolean isSourceFileRunnable(SourceFileHolderResourcePath sourceFile, SourceFileModel sourceFileModel);
    
}
