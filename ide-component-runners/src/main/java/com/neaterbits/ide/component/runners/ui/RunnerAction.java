package com.neaterbits.ide.component.runners.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExecutionException;
import com.neaterbits.ide.component.common.action.ActionComponentAppParameters;
import com.neaterbits.ide.component.common.action.ActionComponentExeParameters;
import com.neaterbits.ide.component.common.action.ComponentAction;
import com.neaterbits.ide.component.common.runner.RunnableLanguage;
import com.neaterbits.ide.component.common.runner.RunnerComponent;
import com.neaterbits.ide.component.runners.RunnerHelper;
import com.neaterbits.ide.component.runners.RunnersComponent;
import com.neaterbits.ide.component.runners.RunnersConfigurationHelper;
import com.neaterbits.ide.component.runners.model.RunConfiguration;
import com.neaterbits.ide.component.runners.model.RunnersConfiguration;

final class RunnerAction extends ComponentAction {

    private final RunnerComponent runnerComponent;
    
    RunnerAction(RunnerComponent runnerComponent) {
        super(runnerComponent.getClass(), RunnersComponentUI.TRANSLATION_ID_RUN_AS);

        Objects.requireNonNull(runnerComponent);

        this.runnerComponent = runnerComponent;
    }

    @Override
    public void execute(ActionComponentExeParameters parameters) throws ActionExecutionException {

        final SourceFileResourcePath runFile = parameters.getCurrentSourceFileResourcePath();

        // Must store this under configuration for that project 
        final ProjectModuleResourcePath runProject = runFile.getModule().getRoot();
        
        RunnersConfiguration runnersConfiguration;

        try {
            runnersConfiguration = parameters.getComponentIDEAccess().readConfigurationFile(
                    RunnersComponent.class,
                    RunnersConfiguration.class,
                    runProject);
        } catch (IOException ex) {
            throw new ActionExecutionException("Unable to read configuration", ex);
        }
        
        if (runnersConfiguration == null) {
            // File did not exist so create a new one
            runnersConfiguration = new RunnersConfiguration();
            runnersConfiguration.setRunConfigurations(new ArrayList<>());
        }

        final RunConfiguration runConfiguration = RunnerHelper.run(
                runnerComponent,
                runFile,
                parameters,
                runnersConfiguration);

        if (runConfiguration != null) {
            // Store default configuration

            if (runnersConfiguration.getRunConfigurations().contains(runConfiguration)) {
                throw new IllegalStateException();
            }
            
            runnersConfiguration.getRunConfigurations().add(runConfiguration);
            
            try {
                RunnersConfigurationHelper.splitAndSaveRunnerConfiguration(
                        parameters.getComponentIDEAccess(),
                        runnersConfiguration.getRunConfigurations());
            } catch (IOException ex) {
                parameters.getComponentIDEAccess().displayError(
                        "Error while updating runners configuration",
                         ex);
            }
        }
    }
    
    @Override
    public boolean isApplicableInContexts(
            ActionComponentAppParameters parameters,
            ActionContexts focusedContexts,
            ActionContexts allContexts) {
        
        final SourceFileResourcePath sourceFile = parameters.getCurrentSourceFileResourcePath();
        
        boolean fileRunnable;

        if (sourceFile != null) {
            final RunnableLanguage runnableLanguage
                = parameters.getSourceFileLanguage(RunnableLanguage.class, sourceFile);
            
            final SourceFileModel sourceFileModel = parameters.getSourceFileModel(sourceFile);
        
            fileRunnable = sourceFileModel != null && runnerComponent.isRunnable(
                        sourceFile,
                        sourceFileModel,
                        runnableLanguage);
       
            if (!fileRunnable) {
                fileRunnable = RunnerHelper.isClassBytecodeFileRunnable(sourceFile, runnerComponent, parameters);
            }
        }
        else {
            fileRunnable = false;
        }

        return fileRunnable;
    }
}
