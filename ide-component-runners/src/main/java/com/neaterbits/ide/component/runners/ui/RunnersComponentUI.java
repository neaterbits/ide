package com.neaterbits.ide.component.runners.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.ide.common.ui.menus.BuiltinMenu;
import com.neaterbits.ide.common.ui.menus.MenuBuilder;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;
import com.neaterbits.ide.component.common.action.ActionComponentExeParameters;
import com.neaterbits.ide.component.common.runner.RunnerComponent;
import com.neaterbits.ide.component.common.ui.ComponentDialogContext;
import com.neaterbits.ide.component.common.ui.DialogComponentUI;
import com.neaterbits.ide.component.common.ui.MenuComponentUI;
import com.neaterbits.ide.component.runners.RunnersComponent;
import com.neaterbits.ide.component.runners.RunnersConfigurationHelper;
import com.neaterbits.ide.component.runners.model.RunConfiguration;
import com.neaterbits.ide.component.runners.model.RunnersConfiguration;
import com.neaterbits.ide.ui.swt.SWTDialogUIContext;
import com.neaterbits.ide.util.swt.SWTDialogs;

public final class RunnersComponentUI implements DialogComponentUI, MenuComponentUI {

    public static final String TRANSLATION_ID_RUN = "run";

    public static final String TRANSLATION_ID_MAIN = "main";
    public static final String TRANSLATION_ID_MAIN_PROJECT = "main_project";
    public static final String TRANSLATION_ID_MAIN_CLASS = "main_class";

    public static final String TRANSLATION_ID_ARGUMENTS = "arguments";
    public static final String TRANSLATION_ID_ARGUMENTS_PROGRAM = "arguments_program";
    public static final String TRANSLATION_ID_ARGUMENTS_VM = "arguments_vm";
    
    public static final String TRANSLATION_ID_ENVIRONMENT = "environment";
    
    @Override
    public void openDialog(
            ComponentDialogContext dialogContext,
            ActionComponentExeParameters parameters) {

        final SWTDialogUIContext dialogUIContext = (SWTDialogUIContext)dialogContext;

        RunnersConfiguration runnersConfiguration = null;

        final ComponentIDEAccess componentIDEAccess = parameters.getComponentIDEAccess();

        try {
            runnersConfiguration = RunnersConfigurationHelper.readAndMergeRunnerConfiguration(componentIDEAccess);
        }
        catch (IOException ex) {
            SWTDialogs.displayError(
                    dialogUIContext.getWindow(),
                    "Error while loading runners configuration",
                    ex);
        }
        
        if (runnersConfiguration == null) {
            runnersConfiguration = new RunnersConfiguration();
            runnersConfiguration.setRunConfigurations(new ArrayList<>());
        }
        
        final Function<RunConfiguration, String> getMainClassText
            = runConfiguration -> {
              
                final CompileableLanguage language
                    = componentIDEAccess.getLanguages().getSourceFileLanguage(
                                                                CompileableLanguage.class,
                                                                runConfiguration.getSourceFile());
                
                return language.getCompleteNameString(runConfiguration.getMainClass());
            };

        final List<RunnerComponent> runnerComponents
            = parameters.getComponentIDEAccess().findComponents(RunnerComponent.class);
        
        final SWTRunnableDialog runnableDialog = new SWTRunnableDialog(
                dialogUIContext.getWindow(),
                runnersConfiguration.getRunConfigurations(),
                runnerComponents,
                parameters,
                getMainClassText);

        runnableDialog.open();

        final List<RunConfiguration> updated = runnableDialog.getRunConfigurations();
        
        if (runnersConfiguration.getRunConfigurations() == updated) {
            throw new IllegalStateException();
        }

        try {
            RunnersConfigurationHelper.splitAndSaveRunnerConfiguration(componentIDEAccess, updated);
        } catch (IOException ex) {
            SWTDialogs.displayError(
                    dialogUIContext.getWindow(),
                    "Error while updating runners configuration",
                        ex);
        }
    }
    
    public static final String TRANSLATION_ID_RUN_AS = "run_as";
    
    public static final String TRANSLATION_ID_RUN_CONFIGURATIONS = "run_configurations";

    @Override
    public void addToMenu(IDEComponentsConstAccess componentsAccess, MenuBuilder menuBuilder) {

        final List<RunnerComponent> runnerComponents
            = componentsAccess.findComponents(RunnerComponent.class);

        menuBuilder.addToMenu(
                BuiltinMenu.RUN,
                runMenuBuilder ->
                    runMenuBuilder.addSubMenu(
                            Translator.getComponentNamespace(RunnersComponent.class),
                            TRANSLATION_ID_RUN_AS,
                            b -> {
                        for (RunnerComponent runnerComponent : runnerComponents) {
                            b.addAction(new RunnerAction(runnerComponent));
                        }
                    })
                    .addAction(
                            new RunConfigurationsAction(
                                    RunnersComponent.class,
                                    TRANSLATION_ID_RUN_CONFIGURATIONS)));
    }
}
