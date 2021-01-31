package com.neaterbits.ide.component.application.runner;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.component.common.runner.RunnableLanguage;
import com.neaterbits.ide.component.common.runner.RunnerComponent;

public final class MainApplicationRunnerComponent implements RunnerComponent {

    @Override
    public boolean isRunnable(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            RunnableLanguage sourceFileLanguage) {

        return sourceFileLanguage.isSourceFileRunnable(sourceFile, sourceFileModel);
    }

    @Override
    public TypeName getRunnableType(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {

        return sourceFileLanguage.getRunnableType(bytecode);
    }

    @Override
    public boolean isRunnable(
            CompiledFileResourcePath compiledFile,
            ClassBytecode bytecode,
            RunnableLanguage sourceFileLanguage) {
        
        return sourceFileLanguage.isBytecodeRunnable(bytecode);
    }

    @Override
    public String[] getCommandLineForRunning(
            SourceFileResourcePath sourceFile,
            SourceFileModel sourceFileModel,
            TypeName entryPointType,
            String[] programArguments,
            String[] vmArguments,
            BuildRoot buildRoot) {

        return RunnerComponent.getCommandLineFromArguments(
                sourceFile,
                sourceFileModel,
                entryPointType,
                programArguments,
                vmArguments,
                buildRoot);
    }
}
