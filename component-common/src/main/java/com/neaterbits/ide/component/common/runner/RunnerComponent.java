package com.neaterbits.ide.component.common.runner;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.component.common.IDEComponent;

public interface RunnerComponent extends IDEComponent {

    boolean isRunnable(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            RunnableLanguage sourceFileLanguage);

    boolean isRunnable(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage);

    TypeName getRunnableType(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage);

    String [] getCommandLineForRunning(
        SourceFileResourcePath sourceFile,
        SourceFileModel sourceFileModel,
        TypeName entryPointType,
        String [] programArguments,
        String [] vmArguments,
        BuildRoot buildRoot);
}
