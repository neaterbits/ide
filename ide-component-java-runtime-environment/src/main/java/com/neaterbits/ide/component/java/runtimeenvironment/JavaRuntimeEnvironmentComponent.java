package com.neaterbits.ide.component.java.runtimeenvironment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.language.java.jdk.JavaRuntimeEnvironment;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.component.common.language.LanguageName;
import com.neaterbits.ide.component.common.runtimeenvironment.RuntimeEnvironmentComponent;
import com.neaterbits.ide.component.java.language.JavaLanguageComponent;

public final class JavaRuntimeEnvironmentComponent implements RuntimeEnvironmentComponent {
    
    private final JavaRuntimeEnvironment runtimeEnvironment;

    public JavaRuntimeEnvironmentComponent(JavaRuntimeEnvironment runtimeEnvironment) {
        
        Objects.requireNonNull(runtimeEnvironment);

        this.runtimeEnvironment = runtimeEnvironment;
    }

    @Override
    public String[] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments) {
        
        return runtimeEnvironment.getCommandLineForRunning(
                                        projects,
                                        libraries,
                                        entryPointType,
                                        programArguments,
                                        vmArguments);
    }

    @Override
    public List<LanguageName> getSupportedLanguages() {
        
        return 
                Arrays.asList(
                        JavaLanguageComponent.LANGUAGE_NAME);
    }
}
