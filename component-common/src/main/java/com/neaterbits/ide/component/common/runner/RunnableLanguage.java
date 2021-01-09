package com.neaterbits.ide.component.common.runner;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.ide.common.model.source.SourceFileModel;

public interface RunnableLanguage {

    boolean isSourceFileRunnable(SourceFileResourcePath sourceFile, SourceFileModel sourceFileModel);
 
    TypeName getRunnableType(ClassBytecode bytecode);

    boolean isBytecodeRunnable(ClassBytecode bytecode);
}
