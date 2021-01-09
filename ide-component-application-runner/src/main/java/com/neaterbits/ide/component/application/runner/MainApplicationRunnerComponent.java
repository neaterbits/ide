package com.neaterbits.ide.component.application.runner;

import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.model.runtimeenvironment.RuntimeEnvironment;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
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
            String [] programArguments,
            String [] vmArguments,
            BuildRoot buildRoot) {
        
        final ProjectModuleResourcePath module = sourceFile.getModule();
        
        final RuntimeEnvironment runtimeEnvironment = buildRoot.getRuntimeEnvironment(module);
        
        final List<CompiledModuleFileResourcePath> projectDeps
                = buildRoot.getProjectDependenciesForProjectModule(module).stream()
                        .map(dep -> {
                            final ProjectModuleResourcePath depPath = dep.getModulePath();
                            
                            return buildRoot.getCompiledModuleFile(depPath);
                        })
                        .collect(Collectors.toList());
        
        final List<LibraryResourcePath> libraryDeps
                = buildRoot.getLibraryDependenciesForProjectModule(module).stream()
                        .map(LibraryDependency::getModulePath)
                        .collect(Collectors.toList());
        
        return runtimeEnvironment.getCommandLineForRunning(
                                            projectDeps,
                                            libraryDeps,
                                            entryPointType,
                                            programArguments,
                                            vmArguments);
    }
}
