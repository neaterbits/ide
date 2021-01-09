package com.neaterbits.ide.component.common.runtimeenvironment;

import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.component.common.language.LanguageName;

public interface RuntimeEnvironmentComponent {

    List<LanguageName> getSupportedLanguages();

    String [] getCommandLineForRunning(
            List<CompiledModuleFileResourcePath> projects,
            List<LibraryResourcePath> libraries,
            TypeName entryPointType,
            String [] programArguments,
            String [] vmArguments);

}
